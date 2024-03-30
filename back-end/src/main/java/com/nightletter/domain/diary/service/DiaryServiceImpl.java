package com.nightletter.domain.diary.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.nightletter.domain.diary.dto.DiaryCreateRequest;
import com.nightletter.domain.diary.dto.DiaryListRequest;
import com.nightletter.domain.diary.dto.DiaryListResponse;
import com.nightletter.domain.diary.dto.DiaryRequestDirection;
import com.nightletter.domain.diary.dto.RecommendDataResponse;
import com.nightletter.domain.diary.dto.RecommendResponse;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryOpenType;
import com.nightletter.domain.diary.entity.DiaryTarot;
import com.nightletter.domain.diary.entity.DiaryTarotId;
import com.nightletter.domain.diary.entity.DiaryTarotType;
import com.nightletter.domain.diary.repository.DiaryRepository;
import com.nightletter.domain.diary.repository.DiaryTarotRepository;
import com.nightletter.domain.member.repository.MemberRepository;
import com.nightletter.domain.tarot.dto.TarotDto;
import com.nightletter.domain.tarot.service.TarotService;

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
	public Optional<RecommendResponse> createDiary(DiaryCreateRequest diaryRequest) {

		RecommendDataResponse recommendDataResponse = webClient.post()
			.uri("/diaries/recommend")
			.body(BodyInserters.fromValue(Map.of("content", diaryRequest.getContent())))
			.retrieve()
			.bodyToMono(RecommendDataResponse.class)
			.doOnError(error -> log.error("Fast API CONNECT ERROR: {}", error.getMessage()))
			.onErrorResume(error -> Mono.empty())
			.block();

		RecommendResponse recommendResponse = new RecommendResponse(); // 응답

		recommendResponse.setRecommendDiaries(diaryRepository
			.findRecommendDiaries(recommendDataResponse.getDiariesId()));

		TarotDto similarTarot = tarotService.findSimilarTarot(recommendDataResponse.getVector());
		recommendResponse.setCard(similarTarot);


		// todo. Security  적용
		Diary saveDiary = diaryRepository.save(diaryRequest.toEntity());
		DiaryTarotId diaryTarotId = new DiaryTarotId(saveDiary.getId(), similarTarot.id());
		diaryTarotRepository.save(new DiaryTarot(diaryTarotId, DiaryTarotType.NOW));

		return Optional.of(recommendResponse);
	}

	@Override
	public Optional<Diary> updateDiaryDisclosure(Integer diaryId, DiaryOpenType diaryOpenType) {
		Diary diary = null;

		try {
			diary = diaryRepository.getReferenceById(diaryId);
			return Optional.of(diary.modifyDiaryDisclosure(diaryOpenType));
		} catch (Exception e) {
			log.info("Error Occured: " + e.toString());
		}
		return Optional.empty();
	}

	@Override
	public Optional<DiaryListResponse> findDiaries(DiaryListRequest request) {
		// User Id 가져오는 부분. 이후 수정 필요.

		LocalDate querySttDate = request.getDate();
		LocalDate queryEndDate = request.getDate();

		if (request.getDirection() == DiaryRequestDirection.BOTH ||
			request.getDirection() == DiaryRequestDirection.BEFORE) {
			querySttDate = querySttDate.minusDays(request.getSize());
		}
		if (request.getDirection() == DiaryRequestDirection.BOTH ||
			request.getDirection() == DiaryRequestDirection.AFTER) {
			queryEndDate = queryEndDate.plusDays(request.getSize());
		}

		List<Diary> diaries = diaryRepository.findDiariesByMemberId(getCurrentMemberId(), querySttDate, queryEndDate);

		DiaryListResponse diaryListResponse = new DiaryListResponse();

		diaryListResponse.setDiaries(diaries.stream().map(Diary::toDiaryResponse).toList());

		return Optional.of(diaryListResponse);
	}

	private Long getCurrentMemberId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return Long.parseLong((String)authentication.getPrincipal());
	}
}
