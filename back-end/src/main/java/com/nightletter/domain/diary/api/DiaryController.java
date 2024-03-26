package com.nightletter.domain.diary.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nightletter.domain.diary.dto.DiaryRequest;
import com.nightletter.domain.diary.dto.DiaryUpdateRequest;
import com.nightletter.domain.diary.entity.Diary;
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
	public ResponseEntity<?> addDiary(@RequestBody DiaryRequest diaryRequest) {
		Diary diary = diaryService.createDiary(diaryRequest);

		if (diary == null){
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(diary);
	}

	@PatchMapping("/diaries")
	public ResponseEntity<?> modifyDiary(@RequestBody DiaryUpdateRequest diaryUpdateRequest) {
		Diary diary = diaryService.updateDiary(diaryUpdateRequest);

		if (diary == null){
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(diary);
	}
}
