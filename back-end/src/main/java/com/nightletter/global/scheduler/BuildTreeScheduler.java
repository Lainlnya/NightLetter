package com.nightletter.global.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.nightletter.global.utils.connection.FastApiConnection;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class BuildTreeScheduler {
	private final WebClient webClient;
	private final FastApiConnection connection;

	// @Scheduled(cron = "0 0 0/2 * * *")
	// @PostConstruct
	// private void buildTreeSchedule() {
	// 	connection.isServerAvailable()
	// 		.subscribe(isAvailable -> {
	// 		if (isAvailable) {
	// 			buildTree();
	// 		} else {
	// 			log.error("Fast API Server is down. ");
	// 		}
	// 	});
	// }
	//
	// private void buildTree() {
	// 	webClient.post()
	// 		.uri("/diaries/tree")
	// 		.retrieve()
	// 		.bodyToMono(String.class)
	// 		.doOnSuccess(response -> log.info("Response: {}", response))
	// 		.doOnError(error -> log.error("Error occurred: ", error))
	// 		.onErrorResume(error -> Mono.empty())
	// 		.subscribe();
	// }
}
