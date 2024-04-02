package com.nightletter.domain.diary.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nightletter.domain.diary.dto.DiaryCommentRequest;
import com.nightletter.domain.diary.dto.DiaryCommentResponse;
import com.nightletter.domain.diary.dto.GPTResponse;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryTarot;
import com.nightletter.domain.diary.repository.DiaryRepository;
import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GptServiceImpl {

	@Value("${chatgpt.api-key}")
	private String api_key;

	private final DiaryRepository diaryRepository;
	private final MemberRepository memberRepository;

	private static final RestTemplate restTemplate = new RestTemplate();

	public String askQuestion(String question) {
		DiaryCommentResponse response = getResponse(
			buildHttpEntity(
				new DiaryCommentRequest(
					"gpt-3.5-turbo-instruct",
					question,
					1000,
					0.5,
					1.0
				)
			)
		);
		return response.getChoices().get(0).getText();
	}

	private DiaryCommentResponse getResponse(HttpEntity<DiaryCommentRequest> chatGptRequestDtoHttpEntity) {
		return restTemplate.postForEntity(
				"https://api.openai.com/v1/completions",
				chatGptRequestDtoHttpEntity,
				DiaryCommentResponse.class)
			.getBody();
	}

	private HttpEntity<DiaryCommentRequest> buildHttpEntity(DiaryCommentRequest requestDto) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + api_key);
		return new HttpEntity<>(requestDto, headers);
	}

	public Optional<GPTResponse> findGptComment() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Member member = memberRepository.findByMemberId(Integer.parseInt((String)authentication.getPrincipal()));
		Diary diary = diaryRepository.findByDateAndWriter(LocalDate.now(), member);

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
}
