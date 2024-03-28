package com.nightletter.global.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class BuildTreeScheduler {
	private final WebClient webClient;

	@Scheduled(cron = "0 0 */2 * * *")
	@PostConstruct
	public void buildTree() {
		webClient.post()
			.uri("/diaries/tree")
			.retrieve()
			.bodyToMono(String.class)
			.doOnSuccess(response -> log.info("Response: {}", response))
			.doOnError(error -> log.error("Error occurred: ", error))
			.subscribe();
	}
}
