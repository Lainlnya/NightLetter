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
		TarotResponse tarotDto = TarotResponse.builder()
			.name("The Fool")
			.imgUrl("https://ssafy-tarot-01.s3.ap-northeast-2.amazonaws.com/forward/0.png")
			.type(DiaryTarotType.FUTURE)
			.desc("정방향 바보 카드는 당신이 예상치 못한 흥미로운 새 모험의 직전에 있다는 것을 나타냅니다.  ㅋㅋ \n")
			.keyword("시작, 자유, 무모함, 모험, 가능성")
			.build();
		tarotService.findFutureTarot();
		return ResponseEntity.status(HttpStatus.OK).body(tarotDto);
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
