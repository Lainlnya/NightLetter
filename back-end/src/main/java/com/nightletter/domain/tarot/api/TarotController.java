package com.nightletter.domain.tarot.api;

import static com.nightletter.global.common.ResponseDto.*;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nightletter.domain.tarot.dto.PastTarotResponse;
import com.nightletter.domain.tarot.dto.TarotDto;
import com.nightletter.domain.tarot.entity.TarotDirection;
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
	public ResponseEntity<?> findFutureTarot() {
		TarotDto tarotDto = new TarotDto(1, "The Fool",
			"https://ssafy-tarot-01.s3.ap-northeast-2.amazonaws.com/forward/0.png",
			"시작, 자유, 무모함, 모험, 가능성",
			"정방향 바보 카드는 당신이 예상치 못한 흥미로운 새 모험의 직전에 있다는 것을 나타냅니다.\n"
				+ "이 여정은 맹목적일 수 있으나, 당신의 성장에 기여할 수 있는 보람된 경험이 될 것입니다. ",
			TarotDirection.FORWARD, null);
		return ResponseEntity.status(HttpStatus.OK).body(tarotDto);
	}

	@GetMapping("/past")
	public ResponseEntity<?> findPastTarot() {

		return tarotService.getRandomPastTarot().map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/past")
	public ResponseEntity<?> addPastTarot() {

		Optional<PastTarotResponse> response = tarotService.createRandomPastTarot();

		if (response.isEmpty())
			return databaseError();
		return ResponseEntity.ok(response);
	}
}
