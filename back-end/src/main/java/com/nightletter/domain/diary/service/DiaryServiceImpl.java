package com.nightletter.domain.diary.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

		Diary userDiary = diaryRequest.toEntity(getCurrentMember(), embedVector);
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
}
