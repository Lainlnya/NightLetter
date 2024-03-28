package com.nightletter.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	@Bean
	public WebClient webClient() {
		return WebClient.builder()
			.baseUrl("http://127.0.0.1:8000")
			.exchangeStrategies(ExchangeStrategies.builder()
				.codecs(configurer -> configurer
					.defaultCodecs()
					.maxInMemorySize(16 * 1024 * 1024)) // 16MB
				.build())
			.build();
	}
}