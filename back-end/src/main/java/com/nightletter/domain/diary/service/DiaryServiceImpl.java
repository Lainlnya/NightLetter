package com.nightletter.domain.diary.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nightletter.domain.diary.dto.DiaryCreateRequest;
import com.nightletter.domain.diary.dto.DiaryDisclosureRequest;
import com.nightletter.domain.diary.dto.DiaryListRequest;
import com.nightletter.domain.diary.dto.DiaryListResponse;
import com.nightletter.domain.diary.dto.DiaryRequestDirection;
import com.nightletter.domain.diary.dto.DiaryResponse;
import com.nightletter.domain.diary.dto.RecommendDataResponse;
import com.nightletter.domain.diary.dto.RecommendResponse;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryTarot;
import com.nightletter.domain.diary.entity.DiaryTarotId;
import com.nightletter.domain.diary.entity.DiaryTarotType;
import com.nightletter.domain.diary.repository.DiaryRepository;
import com.nightletter.domain.diary.repository.DiaryTarotRepository;
import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.member.repository.MemberRepository;
import com.nightletter.domain.tarot.dto.TarotDto;
import com.nightletter.domain.tarot.service.TarotService;
import com.nightletter.global.common.ResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

	private final DiaryRepository diaryRepository;
	private final DiaryTarotRepository diaryTarotRepository;
	private final WebClient webClient;
	private final TarotService tarotService;
	private final MemberRepository memberRepository;

	@Override
	public Optional<RecommendResponse> createDiary(DiaryCreateRequest diaryRequest) throws JsonProcessingException {

		RecommendDataResponse recommendDataResponse = webClient.post()
			.uri("/diaries/recommend")
			.body(BodyInserters.fromValue(Map.of("content", diaryRequest.getContent())))
			.retrieve()
			.bodyToMono(RecommendDataResponse.class)
			.doOnError(error -> log.error("Fast API CONNECT ERROR: {}", error.getMessage()))
			.onErrorResume(error -> Mono.empty())
			.block();

		RecommendResponse recommendResponse = new RecommendResponse(); // 응답
		log.info("================== vec : {}", recommendDataResponse.getEmbedVector().getEmbed().size());
		log.info("================== diary ids : {}", recommendDataResponse.getDiariesId().size());
		recommendResponse.setRecommendDiaries(diaryRepository
			.findRecommendDiaries(recommendDataResponse.getDiariesId()));

		TarotDto similarTarot = tarotService.findSimilarTarot(recommendDataResponse.getEmbedVector());
		recommendResponse.setCard(similarTarot);

		// todo. Security  적용
		Diary saveDiary = diaryRepository.save(diaryRequest.toEntity(getCurrentMember(), recommendDataResponse.getEmbedVector()));
		DiaryTarotId diaryTarotId = new DiaryTarotId(saveDiary.getDiaryId(), similarTarot.id());
		diaryTarotRepository.save(new DiaryTarot(diaryTarotId, DiaryTarotType.NOW));

		return Optional.of(recommendResponse);
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
