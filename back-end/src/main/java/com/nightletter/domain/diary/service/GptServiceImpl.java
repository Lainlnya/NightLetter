package com.nightletter.domain.diary.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nightletter.domain.diary.dto.request.DiaryCommentRequest;
import com.nightletter.domain.diary.dto.response.DiaryCommentResponse;
import com.nightletter.domain.diary.dto.response.GPTResponse;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryTarot;
import com.nightletter.domain.diary.repository.DiaryRepository;
import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GptServiceImpl {

	private static final RestTemplate restTemplate = new RestTemplate();
	private final DiaryRepository diaryRepository;
	private final MemberRepository memberRepository;
	@Value("${chatgpt.api-key}")
	private String api_key;
	@Value("${chatgpt.model}")
	private String model;

	public String askQuestion(String question) {
		return this.getResponse(
			buildHttpEntity(
				new DiaryCommentRequest(
					model,
					question
				)
			)
		).getChoices().get(0).getMessage().getContent().toString();
	}

	private DiaryCommentResponse getResponse(HttpEntity<DiaryCommentRequest> chatGptRequestDtoHttpEntity) {
		return restTemplate.postForEntity(
				"https://api.openai.com/v1/chat/completions",
				chatGptRequestDtoHttpEntity,
				DiaryCommentResponse.class)
			.getBody();
	}

	private HttpEntity<DiaryCommentRequest> buildHttpEntity(DiaryCommentRequest requestDto) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
		headers.add("Authorization", "Bearer " + api_key);
		return new HttpEntity<>(requestDto, headers);
	}

	public Optional<GPTResponse> findGptComment() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Member member = memberRepository.findByMemberId(Integer.parseInt((String)authentication.getPrincipal()));
		// Diary diary = diaryRepository.findByDateAndWriter(LocalDate.now(), member);
		List<Diary> diaries = diaryRepository.findAllByDateAndWriter(getToday(), member);
		Diary diary = diaries.get(0);

		if (diary == null) {
			return Optional.empty();
		}

		GPTResponse response = new GPTResponse();

		for (DiaryTarot dt : diary.getDiaryTarots()) {
			switch (dt.getType()) {
				case PAST:
					response.setPast_url(dt.getTarot().getImgUrl());
					break;
				case NOW:
					response.setNow_url(dt.getTarot().getImgUrl());
					break;
				case FUTURE:
					response.setFuture_url(dt.getTarot().getImgUrl());
					break;
			}
		}

		response.setGptComment(diary.getGptComment());
		return Optional.of(response);

	}

	private LocalDate getToday() {
		return LocalTime.now().isAfter(LocalTime.of(4, 0)) ?
			LocalDate.now() : LocalDate.now().minusDays(1);
	}
}
