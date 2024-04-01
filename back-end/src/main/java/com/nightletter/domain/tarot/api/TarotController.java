package com.nightletter.domain.tarot.api;

import static com.nightletter.global.common.ResponseDto.*;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nightletter.domain.diary.entity.DiaryTarotType;
import com.nightletter.domain.tarot.dto.TarotResponse;
import com.nightletter.domain.tarot.service.TarotService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tarots")
public class TarotController {

	private final TarotService tarotService;

	@GetMapping("/future")
	public ResponseEntity<TarotResponse> findFutureTarot() {
		TarotResponse futureTarot = tarotService.findFutureTarot();
		return ResponseEntity.status(HttpStatus.OK).body(futureTarot);
	}

	@GetMapping("/past")
	public ResponseEntity<?> findPastTarot() {

		return tarotService.getRandomPastTarot().map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/past")
	public ResponseEntity<?> addPastTarot() {

		Optional<TarotResponse> response = tarotService.createRandomPastTarot();

		if (response.isEmpty())
			return databaseError();
		return ResponseEntity.ok(response);
	}
}
