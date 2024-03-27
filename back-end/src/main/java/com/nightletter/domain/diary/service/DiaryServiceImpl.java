package com.nightletter.domain.diary.service;

import com.nightletter.domain.diary.dto.*;
import com.nightletter.domain.diary.entity.*;
import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nightletter.domain.diary.repository.DiaryRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service @Slf4j
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

	private final DiaryRepository diaryRepository;
	private final MemberRepository memberRepository;
	@Override
	public Optional<Diary> createDiary(DiaryCreateRequest diaryCreateRequest) {
		return Optional.of(diaryRepository.save(diaryCreateRequest.toEntity()));
	}

	@Override
	public Optional<Diary> updateDiaryDisclosure(Long diaryId, DiaryType diaryType) {
		Diary diary = null;

		try {
			diary = diaryRepository.getReferenceById(diaryId);
			return Optional.of(diary.modifyDiaryDisclosure(diaryType));
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
