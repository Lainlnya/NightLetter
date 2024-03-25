package com.nightletter.apis.controller;


import com.nightletter.apis.service.DiaryService;
import com.nightletter.db.entity.Diary;
import com.nightletter.db.request.DiaryRequest;
import com.nightletter.db.request.DiaryUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/diary")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping("/diaries")
    public ResponseEntity<?> addDiary(@RequestBody DiaryRequest diaryRequest) {

        Diary diary = diaryService.createDiary(diaryRequest);

        if (diary == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(diary);
    }

    @PatchMapping("/diaries")
    public ResponseEntity<?> modifyDiary(@RequestBody DiaryUpdateRequest diaryUpdateRequest) {

        log.info("==================================");
        log.info(diaryUpdateRequest.toString());

        Diary diary = diaryService.updateDiary(diaryUpdateRequest);

        if (diary == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(diary);
    }
}
