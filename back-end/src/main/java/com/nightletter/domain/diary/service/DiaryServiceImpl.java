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
import com.nightletter.domain.diary.dto.DiaryRequestDirection;
import com.nightletter.domain.diary.dto.DiaryResponse;
import com.nightletter.domain.diary.dto.RecommendDataResponse;
import com.nightletter.domain.diary.dto.RecommendResponse;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryTarotType;
import com.nightletter.domain.diary.repository.DiaryRepository;
import com.nightletter.domain.diary.repository.DiaryTarotRepository;
import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.member.repository.MemberRepository;
import com.nightletter.domain.tarot.dto.TarotDto;
import com.nightletter.domain.tarot.entity.Tarot;
import com.nightletter.domain.tarot.repository.TarotRepository;
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
	private final TarotRepository tarotRepository;
	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public Optional<RecommendResponse> createDiary(DiaryCreateRequest diaryRequest) {

		Member olrlobt = memberRepository.findByMemberId(1);

		RecommendDataResponse recDataResponse = webClient.post()
			.uri("/diaries/recommend")
			.body(BodyInserters.fromValue(Map.of("content", diaryRequest.getContent())))
			.retrieve()
			.bodyToMono(RecommendDataResponse.class)
			.doOnError(error -> log.error("Fast API CONNECT ERROR: {}", error.getMessage()))
			.onErrorResume(error -> Mono.empty())
			.block();

		log.info("======= vec : {},  diary ids : {}",
			recDataResponse.getEmbedVector().getEmbed().size(),
			recDataResponse.getDiariesId().size());

		RecommendResponse recResponse = new RecommendResponse(); // 응답

		recResponse.setRecommendDiaries(diaryRepository
			.findRecommendDiaries(recDataResponse.getDiariesId()));

		TarotDto recommendTarot = tarotService.findSimilarTarot(recDataResponse.getEmbedVector());
		recResponse.setCard(recommendTarot);

		// Diary saveDiary = diaryRepository.save(diaryRequest.toEntity(getCurrentMember(), recDataResponse.getEmbedVector()));
		Diary saveDiary = diaryRepository.save(diaryRequest.toEntity(olrlobt, recDataResponse.getEmbedVector()));
		//todo. 과거 카드 설정
		// DiaryTarot pastTarot = diaryTarotRepository.findByDiary(saveDiary);
		Tarot nowTarot = tarotRepository.findById(recommendTarot.id()).get();
		Tarot pastTarot = tarotRepository.findById(155).get();

		int randomTarotId = tarotService.getRandomTarotId(pastTarot.getId(), nowTarot.getId());
		Tarot futureTarot = tarotRepository.findById(randomTarotId).get();

		saveDiary.addDiaryTarot(pastTarot, DiaryTarotType.PAST);
		saveDiary.addDiaryTarot(nowTarot, DiaryTarotType.NOW);
		saveDiary.addDiaryTarot(futureTarot, DiaryTarotType.FUTURE);
		return Optional.of(recResponse);
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

		List<Diary> diaries = diaryRepository.findDiariesByMember(getCurrentMember(), querySttDate, queryEndDate);

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
