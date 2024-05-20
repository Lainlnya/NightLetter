package com.nightletter.domain.social.api;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nightletter.domain.social.dto.response.GptNotificationResponse;
import com.nightletter.domain.social.dto.response.NotificationResponse;
import com.nightletter.domain.social.dto.response.RecommendNotificationResponse;
import com.nightletter.domain.social.entity.Notification;
import com.nightletter.domain.social.service.NotificationService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/notification")
public class NotificationController {

	private final NotificationService notificationService;


	@GetMapping("/test")
	public ResponseEntity<?> testResponses() {

		List<NotificationResponse> responses = List.of(
			GptNotificationResponse.builder()
				.notificationId(1)
				.title("GPT TITLE")
				.content("GPT CONTENT")
				.isRead(true)
				.test("dsamlkdmaskl")
				.build(),
			RecommendNotificationResponse.builder()
				.notificationId(1)
				.title("REC TITLE")
				.content("REC CONTENT")
				.isRead(true)
				.build()
		);

		return ResponseEntity.ok(responses);
	}

	@GetMapping("")
	public ResponseEntity<?> getAllNotifications() {

		return ResponseEntity.ok(notificationService.getAllNotifications());
	}

	@PatchMapping("")
	public ResponseEntity<?> updateNotificationIsRead(@RequestBody Long notificationId) {

		Optional<NotificationResponse> notificationResponse = notificationService.updateNotificationIsRead(notificationId);

		return notificationResponse
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("")
	public ResponseEntity<?> deleteNotification(@RequestBody Long notificationId) {

		notificationService.deleteNotification(notificationId);

		return ResponseEntity.ok().build();
	}

}
