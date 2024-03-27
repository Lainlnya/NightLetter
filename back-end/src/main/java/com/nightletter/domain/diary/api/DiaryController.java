package com.nightletter.domain.diary.api;

import com.nightletter.domain.diary.entity.DiaryType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nightletter.domain.diary.dto.DiaryCreateRequest;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.service.DiaryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/diary")
public class DiaryController {

	private final DiaryService diaryService;

	@PostMapping("/diaries")
	public ResponseEntity<?> addDiary(@RequestBody DiaryCreateRequest diaryCreateRequest) {
		Optional<Diary> diary = diaryService.createDiary(diaryCreateRequest);

		return diary.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.badRequest().build());
	}

	@PatchMapping("/diaries/{diaryId}")
	public ResponseEntity<?> modifyDiary(@PathVariable Integer diaryId, @RequestParam DiaryType diaryType) {
		Optional<Diary> diary = diaryService.updateDiaryDisclosure(diaryId, diaryType);

		return diary.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.badRequest().build());
	}

	@GetMapping("/diaries")
	public ResponseEntity<?> findDiaries(@RequestBody DiaryCreateRequest diaryCreateRequest) {

		return null;
	}
}
