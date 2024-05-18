package com.nightletter.domain.tarot.api;

import static com.nightletter.global.common.ResponseDto.*;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nightletter.domain.tarot.dto.TarotResponse;
import com.nightletter.domain.tarot.service.TarotService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/tarots")
public class TarotController {

	private final TarotService tarotService;

	@GetMapping("/future")
	public ResponseEntity<TarotResponse> findFutureTarot() {
		TarotResponse futureTarot = tarotService.findFutureTarot();
		return ResponseEntity.status(HttpStatus.OK).body(futureTarot);
	}

	@GetMapping("/past")
	public ResponseEntity<?> findPastTarot() {

		return tarotService.getPastTarot().map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	// TODO REMOVE AFTER TEST
	@GetMapping("/past-test")
	public ResponseEntity<?> findTestPastTarot() {

		return tarotService.findPastTarot().map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/past")
	public ResponseEntity<?> addPastTarot() {

		Optional<TarotResponse> response = tarotService.createRandomPastTarot();

		if (response.isEmpty())
			return databaseError();
		return ResponseEntity.ok(response);
	}

	// @GetMapping("/test")
	public ResponseEntity<?> getFutureTarotTTL() {
		return ResponseEntity.ok(tarotService.getFutureTarot());
	}

	// @PatchMapping("/test-entity")
	public ResponseEntity<?> updateWithNewEntity() {
		return ResponseEntity.ok(tarotService.updateWithNewEntity());
	}

	// @PatchMapping("/test-single")
	public ResponseEntity<?> updateSingleValue() {
		return ResponseEntity.ok(tarotService.updateOnlyFlipped());
	}
}
