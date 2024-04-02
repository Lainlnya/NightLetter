package com.nightletter.domain.diary.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.nightletter.domain.diary.dto.*;
import com.nightletter.domain.diary.entity.DiaryTarot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.nightletter.domain.diary.dto.DiaryCreateRequest;
import com.nightletter.domain.diary.dto.DiaryDisclosureRequest;
import com.nightletter.domain.diary.dto.DiaryListRequest;
import com.nightletter.domain.diary.dto.DiaryListResponse;
import com.nightletter.domain.diary.dto.DiaryResponse;
import com.nightletter.domain.diary.dto.recommend.EmbedVector;
import com.nightletter.domain.diary.dto.recommend.RecommendDataResponse;
import com.nightletter.domain.diary.dto.recommend.RecommendDiaryResponse;
import com.nightletter.domain.diary.dto.recommend.RecommendResponse;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryTarotType;
import com.nightletter.domain.diary.repository.DiaryRepository;
import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.member.repository.MemberRepository;
import com.nightletter.domain.tarot.entity.Tarot;
import com.nightletter.domain.tarot.service.TarotServiceImpl;
import com.nightletter.global.common.ResponseDto;
import com.nightletter.global.exception.CommonErrorCode;
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
	private final WebClient webClient;
	private final TarotServiceImpl tarotService;
	private final MemberRepository memberRepository;


	@Value("${chatgpt.api-key}")
	private String api_key;
	private double temperature=0.5;
	private double top_p=1.0;
	private String url="https://api.openai.com/v1/completions";
	private String model="gpt-3.5-turbo-instruct";
	private int max_token=1000;

	private static RestTemplate restTemplate = new RestTemplate();

	@Override
	@Transactional
	public RecommendResponse createDiary(DiaryCreateRequest diaryRequest) {

		RecommendDataResponse recDataResponse = fetchRecData(diaryRequest);
		List<Long> recDiariesId = recDataResponse.getDiariesId();
		EmbedVector embedVector = recDataResponse.getEmbedVector();

		RecommendResponse recResponse = new RecommendResponse();
		recResponse.setRecommendDiaries(getRecDiaries(recDiariesId));

		Tarot nowTarot = tarotService.findSimilarTarot(embedVector);
		recResponse.setCard(nowTarot);
		Tarot pastTarot = tarotService.findPastTarot(getCurrentMember());
		Tarot futureTarot = tarotService.makeRandomTarot(pastTarot.getId(), nowTarot.getId());

		
		String question=String.format("%s 라는 일기에 타로카드가 과거 : %s 와 현재 : %s 와 미래 : %s 카드를 바탕으로 존댓말로 공감해줘"
				,diaryRequest.getContent(),
				pastTarot.getName(),
				nowTarot.getName(),
				futureTarot.getName());
		String gptComment = askQuestion(question);

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

	private List<RecommendDiaryResponse> getRecDiaries(List<Long> diariesId){
		List<RecommendDiaryResponse> recommendDiaries = diaryRepository
			.findRecommendDiaries(diariesId, getCurrentMember());
		if (recommendDiaries.isEmpty()) {
			throw new ResourceNotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND, "RECOMMEND DIARIES NOT FOUND");
		}
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
	public Optional<DiaryListResponse> findDiaries(DiaryListRequest request) {
		// User Id 가져오는 부분. 이후 수정 필요.

		LocalDate queryDate = request.getDate();

		List<Diary> diaries = diaryRepository.findDiariesByMemberInDir(getCurrentMember(), request);

		DiaryListResponse diaryListResponse = new DiaryListResponse();

		diaryListResponse.setDiaries(diaries.stream().map(DiaryResponse::of).toList());

		return Optional.of(diaryListResponse);
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

	private Member getCurrentMember() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return memberRepository.findByMemberId(Integer.parseInt((String)authentication.getPrincipal()));
	}

	public HttpEntity<DiaryCommentRequest> buildHttpEntity(DiaryCommentRequest requestDto) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("Authorization"));
		headers.add("Authorization", "Bearer"+ api_key);
		return new HttpEntity<>(requestDto, headers);
	}
	public DiaryCommentResponse getResponse(HttpEntity<DiaryCommentRequest> chatGptRequestDtoHttpEntity) {
		ResponseEntity<DiaryCommentResponse> responseEntity = restTemplate.postForEntity(
				url,
				chatGptRequestDtoHttpEntity,
				DiaryCommentResponse.class);

		return responseEntity.getBody();
	}

	public String askQuestion(String question) {
		DiaryCommentResponse response = this.getResponse(
				this.buildHttpEntity(
						new DiaryCommentRequest(
								model,
								question,
								max_token,
								temperature,
								top_p
						)
				)
		);

		String gptResponse = response.getChoices().get(0).getText();
		return gptResponse;
	}

	@Override
	public Optional<GPTResponse> findGptComment(){

		Diary diary = diaryRepository.findByDateAndWriter(LocalDate.now(), getCurrentMember());

		if (diary == null) {
			return Optional.empty();
		}

		GPTResponse response = new GPTResponse();


		for(DiaryTarot dt : diary.getDiaryTarots()){
			switch (dt.getType()){
				case PAST : response.setPast_url(dt.getTarot().getImgUrl()); break;
				case NOW : response.setNow_url(dt.getTarot().getImgUrl()); break;
				case FUTURE : response.setFuture_url(dt.getTarot().getImgUrl()); break;
			}
		}

		response.setGptComment(diary.getGptComment());


		return Optional.of(response);
	}
}
