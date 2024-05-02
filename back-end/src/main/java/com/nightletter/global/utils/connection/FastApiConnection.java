package com.nightletter.global.utils.connection;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FastApiConnection {

	private final WebClient webClient;

	public Mono<Boolean> isServerAvailable() {
		return webClient.get().uri("/rec/v1/health-check")
			.retrieve()
			.bodyToMono(String.class)
			.map(response -> true)
			.onErrorResume(error -> Mono.just(false));
	}
}
