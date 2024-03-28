package com.nightletter.domain.diary.api;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nightletter.domain.diary.dto.DiaryCreateRequest;
import com.nightletter.domain.diary.dto.DiaryCreateResponse;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryOpenType;
import com.nightletter.domain.diary.service.DiaryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/diary")
public class DiaryController {

	private final DiaryService diaryService;

	@PostMapping("/diaries")
	public ResponseEntity<?> addDiary(@RequestBody DiaryCreateRequest diaryCreateRequest) {
		Optional<DiaryCreateResponse> diary = diaryService.createDiary(diaryCreateRequest);

		return diary.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.badRequest().build());
	}

	@PatchMapping("/diaries/{diaryId}")
	public ResponseEntity<?> modifyDiary(@PathVariable Integer diaryId, @RequestParam DiaryOpenType diaryOpenType) {
		Optional<Diary> diary = diaryService.updateDiaryDisclosure(diaryId, diaryOpenType);

		return diary.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.badRequest().build());
	}

	@GetMapping("/diaries")
	public ResponseEntity<?> findDiaries(@RequestBody DiaryCreateRequest diaryCreateRequest) {

		return null;
	}
}
