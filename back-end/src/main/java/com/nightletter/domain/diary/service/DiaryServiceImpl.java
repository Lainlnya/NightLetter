package com.nightletter.domain.diary.service;

import static com.nightletter.global.exception.CommonErrorCode.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.nightletter.domain.diary.dto.recommend.EmbedVector;
import com.nightletter.domain.diary.dto.recommend.RecommendDataResponse;
import com.nightletter.domain.diary.dto.recommend.RecommendDiaryResponse;
import com.nightletter.domain.diary.dto.request.DiaryCreateEvent;
import com.nightletter.domain.diary.dto.request.DiaryCreateRequest;
import com.nightletter.domain.diary.dto.request.DiaryDisclosureRequest;
import com.nightletter.domain.diary.dto.request.DiaryListRequest;
import com.nightletter.domain.diary.dto.response.DiaryRecResponse;
import com.nightletter.domain.diary.dto.response.DiaryResponse;
import com.nightletter.domain.diary.dto.response.DiaryScrapResponse;
import com.nightletter.domain.diary.dto.response.FutureTarotResponse;
import com.nightletter.domain.diary.dto.response.TodayDiaryResponse;
import com.nightletter.domain.diary.dto.response.TodayTarot;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryTarotType;
import com.nightletter.domain.diary.entity.RecommendedDiary;
import com.nightletter.domain.diary.entity.Scrap;
import com.nightletter.domain.diary.repository.DiaryRepository;
import com.nightletter.domain.diary.repository.RecommendedDiaryRepository;
import com.nightletter.domain.diary.repository.ScrapRepository;
import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.member.repository.MemberRepository;
import com.nightletter.domain.social.entity.NotificationType;
import com.nightletter.domain.social.service.NotificationService;
import com.nightletter.domain.tarot.dto.TarotDto;
import com.nightletter.domain.tarot.dto.TarotResponse;
import com.nightletter.domain.tarot.entity.FutureTarot;
import com.nightletter.domain.tarot.entity.Tarot;
import com.nightletter.domain.tarot.repository.TarotFutureRedisRepository;
import com.nightletter.domain.tarot.repository.TarotPastRedisRepository;
import com.nightletter.domain.tarot.service.TarotService;
import com.nightletter.domain.tarot.service.TarotServiceImpl;
import com.nightletter.global.common.ResponseDto;
import com.nightletter.global.exception.CommonErrorCode;
import com.nightletter.global.exception.InvalidParameterException;
import com.nightletter.global.exception.RecsysConnectionException;
import com.nightletter.global.exception.ResourceNotFoundException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

	private final DiaryRepository diaryRepository;
	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final WebClient webClient;
	private final TarotServiceImpl tarotService;
	private final MemberRepository memberRepository;
	private final ScrapRepository scrapRepository;
	private final GptServiceImpl gptServiceImpl;
	private final TarotService tarotServiceImpl;
	private final TarotFutureRedisRepository futureRedisRepository;
	private final TarotPastRedisRepository pastRedisRepository;
	private final RecommendedDiaryRepository recommendedDiaryRepository;
	private final NotificationService notificationService;

	@PersistenceContext
	private EntityManager entityManager;

	private final String SHARING_BASE_URL = "https://dev.letter-for.me/api/v1/diaries/shared/";

	@Override
	@Transactional
	public TarotResponse createDiary(DiaryCreateRequest diaryRequest) {

		// 추천 사연 + 임베딩 벡터 수신
		RecommendDataResponse recDataResponse = fetchRecData(diaryRequest);
		List<Long> recDiariesId = recDataResponse.getDiariesId();
		EmbedVector embedVector = recDataResponse.getEmbedVector();

		// 1. 임베딩 벡터 기반 현재 카드 추출, 응답.
		// TODO: WRAP WITH OPTIONAL
		Tarot nowTarot = tarotService.findSimilarTarot(embedVector);
		Tarot pastTarot = tarotService.findPastTarot()
			.orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND, "PAST TAROT NOT FOUND"));
		Tarot futureTarot = tarotService.makeRandomTarot(pastTarot.getId(), nowTarot.getId());

		// 2. 다이어리 생성. GPT 코멘트는 이후 업데이트.
		Diary userDiary = diaryRequest.toEntity(getCurrentMember(), embedVector);
		userDiary.addDiaryTarot(pastTarot, DiaryTarotType.PAST);
		userDiary.addDiaryTarot(nowTarot, DiaryTarotType.NOW);
		userDiary.addDiaryTarot(futureTarot, DiaryTarotType.FUTURE);

		diaryRepository.save(userDiary);

		// 3. Redis 미래 카드 마스킹 처리. 사용자가 미래카드 조회 시 toggle.

		// TODO 일괄 수정 예정

		futureRedisRepository.save(FutureTarot.unflipped(getCurrentMemberId()));

		DiaryCreateEvent event = DiaryCreateEvent.builder()
			.diaryId(userDiary.getDiaryId())
			.memberId(getCurrentMemberId())
			.recommendedDiaryIdList(recDiariesId)
			.build();

		System.out.println("SEND_EVENT: event: " + event);

		// 메세지 전송.
		// 생성된 다이어리 아이디와, 임베딩 벡터 값을 전달.
		kafkaTemplate.send("create-diary-dev", event);

		return TarotResponse.of(nowTarot, nowTarot.getDir());
	}

	@Transactional
	@KafkaListener(topics = "create-diary-dev", groupId = "recommend_diary-1")
	public void sendRecommendedDiaries(DiaryCreateEvent event) {

		System.out.println("RECEIVE_REC_EVENT: event: " + event);

		if (event == null || event.getRecommendedDiaryIdList() == null) {
			// 에러 처리 .
			return ;
		}

		Member member = memberRepository.findByMemberId(event.getMemberId());

		// 2. 추천 다이어리 저장 및 추천 다이어리 실시간 알림.
		// 추천 다이어리 저장.

		List<Diary> recDiaries = getRecommendedDiaries(event.getRecommendedDiaryIdList());

		for (Diary diary : recDiaries) {
			RecommendedDiary recommendedDiary = RecommendedDiary.builder()
				.diary(diary)
				.member(member)
				.recommendedDate(getToday())
				.build();

			recommendedDiaryRepository.save(recommendedDiary);
		}

		notificationService.sendNotificationToUser(NotificationType.RECOMMEND_DIARIES_ARRIVAL, member);
	}

	@Transactional
	@KafkaListener(topics = "create-diary-dev", groupId = "gpt_diary-1")
	public void sendGPTComment(DiaryCreateEvent event) {

		System.out.println("RECEIVE_GPT_EVENT: event: " + event);


		if (event == null || event.getRecommendedDiaryIdList() == null) {
			// 에러 처리 .
			return ;
		}

		Member member = memberRepository.findByMemberId(event.getMemberId());

		FutureTarotResponse response = diaryRepository.findFutureTarot(event.getDiaryId())
			.orElseThrow(() ->
				new ResourceNotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND, "Diary Not Found"));

		// 3. GPT 코멘트 요청 및 응답 수신 후 실시간 알림.
		// 데이터 베이스에 업데이트

		String question = String.format("%s, %s, %s", response.getFutureTarotName(), response.getFutureTarotDir().toString(), response.getDiaryContent());
		String gptComment = gptServiceImpl.askQuestion(question);

		diaryRepository.updateDiaryGPTComment(event.getDiaryId(), gptComment);

		// 알림 전송, 저장.
		notificationService.sendNotificationToUser(NotificationType.GPT_COMMENT_ARRIVAL, member);
	}


	private RecommendDataResponse fetchRecData(DiaryCreateRequest diaryRequest) {
		RecommendDataResponse recDataResponse = webClient.post()
			.uri("/diaries/recommend")
			.body(BodyInserters.fromValue(Map.of("content", diaryRequest.getContent())))
			.retrieve()
			.bodyToMono(RecommendDataResponse.class)
			.onErrorResume(error ->
				Mono.error(new RecsysConnectionException(CommonErrorCode.REC_SYS_CONNECTION_ERROR)))
			.block();

		assert recDataResponse != null;
		recDataResponse.validation();
		return recDataResponse;
	}

	private List<Diary> getRecommendedDiaries(List<Long> diariesId) {
		List<Diary> recommendDiaries =  diaryRepository.findAllById(diariesId);
		if (recommendDiaries.isEmpty()) {
			throw new ResourceNotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND, "RECOMMEND DIARIES NOT FOUND");
		}
		log.info("============================= {} ", recommendDiaries.toString());
		return recommendDiaries;
	}

	private List<RecommendDiaryResponse> getRecDiaries(List<Long> diariesId) {
		List<RecommendDiaryResponse> recommendDiaries = diaryRepository
			.findRecommendDiaries(diariesId, getCurrentMember());
		if (recommendDiaries.isEmpty()) {
			throw new ResourceNotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND, "RECOMMEND DIARIES NOT FOUND");
		}
		log.info("============================= {} ", recommendDiaries.toString());
		return recommendDiaries;
	}

	@Override
	public Optional<DiaryResponse> updateDiaryDisclosure(DiaryDisclosureRequest request) {

		try {
			System.out.println(request.toString());

			Diary diary = diaryRepository.getReferenceById(request.getDiaryId());

			diary.modifyDiaryDisclosure(request.getType());

			return Optional.of(DiaryResponse.of(diaryRepository.save(diary)));

		} catch (Exception e) {
			log.info("Error Occured: " + e.toString());
		}
		return Optional.empty();
	}

	@Override
	public List<DiaryResponse> findDiaries(DiaryListRequest request) {

		if (request.getEndDate().isBefore(request.getSttDate())) {
			throw new InvalidParameterException(INVALID_PARAMETER, "END_DATE MUST BE SAME OR LATER THAN STT_DATE");
		}

		// 오늘 일자 LIMIT
		if (request.getEndDate().isAfter(getToday())) {
			request.setEndDate(getToday());
		}

		Map<LocalDate, DiaryResponse> diaryMap = diaryRepository
			.findDiariesByMember(getCurrentMember(), request)
			.stream()
			.collect(Collectors
				.toMap(Diary::getDate, Diary::toDiaryResponse));

		LocalDate today = getToday();

		/*
			오늘의 미래카드 조회 여부를 확인.
			- 조회 시 PASS
			- 조회하지 않았으면 정보를 지워 전달.
		 */
		// TODO 존재하지 않는 경우 처리.

		futureRedisRepository.findById(getCurrentMemberId())
			.ifPresent(futureTarot -> {
				if (!futureTarot.getFlipped() && diaryMap.get(today) != null) {
					diaryMap.get(today).setFutureCard(null);
					diaryMap.get(today).setGptComment(null);
				}
			}
		);

		// 쿼리 결과에 오늘이 포함되어야 하고, MAP 내부에 오늘에 대한 값이 없는 경우

		if (! (today.isAfter(request.getEndDate()) || today.isBefore(request.getSttDate()))) {
			if (! diaryMap.containsKey(today)) {
				diaryMap.put(today,
					DiaryResponse.builder()
						.pastCard(getUnfinishedDiaryOfToday())
						.date(today)
						.build()
				);
			}
		}

		return Stream.iterate(request.getSttDate(),
				date -> date.isBefore(request.getEndDate().plusDays(1)), date -> date.plusDays(1))
			.map(date -> diaryMap.getOrDefault(date, DiaryResponse.builder().date(date).build()))
			.toList();
	}

	@Override
	public Optional<DiaryResponse> findDiary(Long diaryId) {

		Diary diary = diaryRepository.findDiaryByDiaryId(diaryId);

		if (diary == null) {
			return Optional.empty();
		}

		return Optional.ofNullable(DiaryResponse.of(diary));
	}

	@Override
	public TodayDiaryResponse isTodayDiaryWritten() {

		List<TodayTarot> tarots = diaryRepository.findTodayDiary(getCurrentMember(), getToday());

		Optional<TodayTarot> pastTarot = tarots.stream()
			.filter(tarot -> tarot.getCardType() == DiaryTarotType.PAST)
			.findFirst();

		if (pastTarot.isEmpty()) {

			TarotDto pastTarotDto = getUnfinishedDiaryOfToday();

			if (pastTarotDto != null) {
				TodayTarot tempPastTarot = TodayTarot.builder()
					.cardNo(pastTarotDto.id())
					.cardName(pastTarotDto.name())
					.cardDir(pastTarotDto.dir())
					.cardImgUrl(pastTarotDto.imgUrl())
					.cardType(DiaryTarotType.PAST)
					.build();

				tarots.add(tempPastTarot);
			}
		}

		TodayDiaryResponse response = TodayDiaryResponse.of(tarots);

		FutureTarot futureTarot = futureRedisRepository.findById(getCurrentMemberId())
			.orElseGet(() ->null);

		if (futureTarot != null && ! futureTarot.getFlipped()) {
			response.setFutureCard(null);
		}

		return response;

	}

	@Override
	public Optional<ResponseDto> deleteDiary(Long diaryId) {

		Diary diary = diaryRepository.findDiaryByDiaryId(diaryId);

		if (diary == null)
			return Optional.empty();

		diaryRepository.delete(diary);

		return Optional.of(
			ResponseDto.builder()
				.code("SU")
				.message("Diary Deleted Successfully.")
				.build());
	}

	@Override
	public Optional<String> createDiaryShareUrl(Long diaryId) {
		// 해당 일기와 유사한 일기 두개 더 추천 받기
		// 추천받은 일기 id 가져오기 []
		List<Long> sharedDiaries = new LinkedList<>();

		// 임시 코드.
		sharedDiaries.add(diaryId);
		sharedDiaries.add(1L);
		sharedDiaries.add(2L);

		Integer memberId = getCurrentMember().getMemberId();

		// 일기 id [] redis에 저장하기.

		// diaryRedisRepository.save(
		// 	DiaryShared.builder()
		// 		.diaries(sharedDiaries)
		// 		.memberId(memberId)
		// 		.build()
		// );

		// URL 반환하기.
		// String requestUrl = SHARING_BASE_URL +

		return Optional.empty();
	}

	@Override
	public Page<DiaryScrapResponse> findScrappedRecommends(Integer pageNo) {
		return diaryRepository.findScrappedDiaryPages(getCurrentMemberId(), pageNo);
	}

	@Transactional
	@Override
	public void scrapDiary(Long diaryId) {

		Member member = getCurrentMember();
		Diary diary = diaryRepository.findById(diaryId)
			.orElseThrow(() ->
				new ResourceNotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND, "Diary Not Found"));

		System.out.println("SAVING ====");

		Scrap scrap = Scrap.builder()
			.member(member)
			.diary(diary)
			.scrappedAt(getToday())
			.build();

		member.scrapDiary(scrap);
		// TODO 중복 에러 해결, 먼저 조회 (Member에서)
	}

	@Transactional
	@Override
	public void unscrapDiary(Long diaryId) {
		Member member = getCurrentMember();
		Diary diary = diaryRepository.findById(diaryId)
			.orElseThrow(() ->
				new ResourceNotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND, "Diary Not Found"));

		long result = scrapRepository.deleteByMemberAndDiary(member, diary);

		// TODO if result is 0, throw a exception

	}

	@Override
	public List<DiaryRecResponse> findTodayRecommendedDiaries() {
		return diaryRepository.findTodayDiaryRecommends(getCurrentMember(), getToday());
	}

	private Member getCurrentMember() {
		return memberRepository.findByMemberId(getCurrentMemberId());
	}

	private Integer getCurrentMemberId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return Integer.parseInt((String)authentication.getPrincipal());
	}

	private LocalDate getToday() {
		return LocalTime.now().isAfter(LocalTime.of(4, 0)) ?
			LocalDate.now() : LocalDate.now().minusDays(1);
	}

	private TarotDto getUnfinishedDiaryOfToday() {

		return tarotServiceImpl.findPastTarot()
				.map(pastTarot -> TarotDto.of(pastTarot, pastTarot.getDir()))
				.orElseGet(() -> null);
	}
}
