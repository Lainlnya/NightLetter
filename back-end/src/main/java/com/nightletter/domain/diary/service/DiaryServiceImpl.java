package com.nightletter.domain.diary.service;

import com.nightletter.domain.diary.dto.*;
import com.nightletter.domain.diary.entity.*;
import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.member.repository.MemberRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nightletter.domain.diary.repository.DiaryRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nightletter.domain.diary.dto.DiaryCreateRequest;
import com.nightletter.domain.diary.dto.DiaryCreateResponse;
import com.nightletter.domain.diary.dto.DiaryListRequest;
import com.nightletter.domain.diary.dto.DiaryListResponse;
import com.nightletter.domain.diary.dto.DiaryRequestDirection;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryOpenType;
import com.nightletter.domain.diary.repository.DiaryRepository;
import com.nightletter.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

	private final DiaryRepository diaryRepository;
	private final WebClient webClient;

	private final MemberRepository memberRepository;

	@Override
	public Optional<DiaryCreateResponse> createDiary(DiaryCreateRequest diaryRequest) {

		DiaryCreateResponse temp = DiaryCreateResponse.createTemp();
		log.info(" create temp file : {}", temp);

		// Mono<JSONArray> responseMono = webClient.post()
		// 	.uri("/get-embedding")
		// 	.body(BodyInserters.fromValue(Map.of("query", diaryRequest.getContent())))
		// 	.retrieve()
		// 	.bodyToMono(JSONArray.class);
		//
		// responseMono.subscribe(
		// 	response -> {
		// 		System.out.println("Response from FastAPI2: " + response);
		// 		diaryRequest.setVector(response.toString());
		// 		diaryRepository.save(diaryRequest.toEntity());
		// 	},
		// 	error -> {
		// 		System.err.println("Error occurred: " + error.getMessage());
		// 	}
		// );
		return Optional.of(temp);
	}

	@Override
	public Optional<Diary> updateDiaryDisclosure(Long diaryId, DiaryOpenType diaryOpenType) {
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

	private Integer getCurrentMemberId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Integer.parseInt((String) authentication.getPrincipal());
	}
}
