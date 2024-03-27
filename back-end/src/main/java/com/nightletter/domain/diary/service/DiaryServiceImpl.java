package com.nightletter.domain.diary.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import net.minidev.json.JSONArray;

import com.nightletter.domain.diary.dto.DiaryListResponse;
import com.nightletter.domain.diary.dto.DiaryRequest;
import com.nightletter.domain.diary.dto.DiaryUpdateRequest;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.repository.DiaryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

	private final DiaryRepository diaryRepository;
	private final WebClient webClient;

	@Override
	public Diary createDiary(DiaryRequest diaryRequest) {
		 Mono<JSONArray> responseMono = webClient.post()
		        .uri("/get-embedding")
		        .body(BodyInserters.fromValue(Map.of("query", diaryRequest.getContent())))
		        .retrieve()
		        .bodyToMono(JSONArray.class);

		responseMono.subscribe(
			response -> {
				System.out.println("Response from FastAPI2: " + response);
				diaryRequest.setVector(response.toString());
				diaryRepository.save(diaryRequest.toEntity());
			},
			error -> {
				System.err.println("Error occurred: " + error.getMessage());
			}
		);
		return null;
	}

	@Override
	public Diary updateDiary(DiaryUpdateRequest diaryUpdateRequest) {
		return diaryRepository.save(diaryUpdateRequest.toEntity());
	}

	@Override
	public DiaryListResponse findDiaries(int id, DiaryRequest diaryRequest) {
		return null;
	}
}
