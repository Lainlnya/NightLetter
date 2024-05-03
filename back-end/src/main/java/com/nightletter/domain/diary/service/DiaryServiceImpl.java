package com.nightletter.domain.diary.service;

import static com.nightletter.global.exception.CommonErrorCode.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.coyote.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.nightletter.domain.diary.dto.recommend.EmbedVector;
import com.nightletter.domain.diary.dto.recommend.RecommendDataResponse;
import com.nightletter.domain.diary.dto.recommend.RecommendDiaryResponse;
import com.nightletter.domain.diary.dto.recommend.RecommendResponse;
import com.nightletter.domain.diary.dto.request.DiaryCreateRequest;
import com.nightletter.domain.diary.dto.request.DiaryDisclosureRequest;
import com.nightletter.domain.diary.dto.request.DiaryListRequest;
import com.nightletter.domain.diary.dto.response.DiaryResponse;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryTarotType;
import com.nightletter.domain.diary.repository.DiaryRedisRepository;
import com.nightletter.domain.diary.repository.DiaryRepository;
import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.member.repository.MemberRepository;
import com.nightletter.domain.tarot.entity.Tarot;
import com.nightletter.domain.tarot.service.TarotServiceImpl;
import com.nightletter.global.common.ResponseDto;
import com.nightletter.global.exception.CommonErrorCode;
import com.nightletter.global.exception.InvalidParameterException;
import com.nightletter.global.exception.RecsysConnectionException;
import com.nightletter.global.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

	private final DiaryRepository diaryRepository;
	private final DiaryRedisRepository diaryRedisRepository;
	private final WebClient webClient;
	private final TarotServiceImpl tarotService;
	private final MemberRepository memberRepository;
	private final GptServiceImpl gptServiceImpl;

	private final String SHARING_BASE_URL = "https://dev.letter-for.me/api/v1/diaries/shared/";

	@Override
	@Transactional
	public RecommendResponse createDiary(DiaryCreateRequest diaryRequest) {

		RecommendDataResponse recDataResponse = fetchRecData(diaryRequest);
		List<Long> recDiariesId = recDataResponse.getDiariesId();
		EmbedVector embedVector = recDataResponse.getEmbedVector();

		RecommendResponse recResponse = new RecommendResponse();
		recResponse.setRecommendDiaries(getRecDiaries(recDiariesId));

		// TODO: WRAP WITH OPTIONAL
		Tarot nowTarot = tarotService.findSimilarTarot(embedVector);
		recResponse.setCard(nowTarot);
		Tarot pastTarot = tarotService.findPastTarot().get();
		Tarot futureTarot = tarotService.makeRandomTarot(pastTarot.getId(), nowTarot.getId());

		String question = String.format("%s, %s, %s", futureTarot.getName(), futureTarot.getDir().toString(), diaryRequest.getContent());
		String gptComment = gptServiceImpl.askQuestion(question);

		Diary userDiary = diaryRequest.toEntity(getCurrentMember(), embedVector);
		userDiary.addDiaryComment(gptComment);
		userDiary.addDiaryTarot(pastTarot, DiaryTarotType.PAST);
		userDiary.addDiaryTarot(nowTarot, DiaryTarotType.NOW);
		userDiary.addDiaryTarot(futureTarot, DiaryTarotType.FUTURE);
		diaryRepository.save(userDiary);
		return recResponse;
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

		Map<LocalDate, DiaryResponse> diaryMap = diaryRepository
			.findDiariesByMember(getCurrentMember(), request)
			.stream()
			.collect(Collectors
				.toMap(Diary::getDate, Diary::toDiaryResponse));

		LocalDate today = getToday();

		// 쿼리 결과에 오늘이 포함되어야 하고, MAP 내부에 오늘에 대한 값이 없는 경우
		if (! (today.isAfter(request.getEndDate()) || today.isBefore(request.getSttDate()))) {
			if (! diaryMap.containsKey(today)) {
				// TODO : 오늘의 데이터를 모아서 반환해야함.
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

	private Member getCurrentMember() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return memberRepository.findByMemberId(Integer.parseInt((String)authentication.getPrincipal()));
	}

	private LocalDate getToday() {
		return LocalTime.now().isAfter(LocalTime.of(4, 0)) ?
			LocalDate.now() : LocalDate.now().minusDays(1);
	}

}
