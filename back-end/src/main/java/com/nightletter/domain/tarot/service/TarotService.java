package com.nightletter.domain.tarot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nightletter.domain.tarot.dto.TarotDto;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.nightletter.domain.tarot.dto.TarotKeyword;
import com.nightletter.domain.tarot.dto.TarotListResponse;
import com.nightletter.domain.tarot.entity.Tarot;
import com.nightletter.domain.tarot.repository.TarotRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TarotService {

	private static final Map<Integer, TarotDto> deck = new HashMap<>();
	private final TarotRepository tarotRepository;
	private final WebClient webClient;

	@PostConstruct
	private void getTarotEmbedded() {
		List<Tarot> allTarots = tarotRepository.findAll();
		List<TarotKeyword> allTarotsKeyword = new ArrayList<>();
		allTarots.forEach(tarot -> allTarotsKeyword.add(tarot.toKeywordDto()));

		TarotListResponse tarotVectors = webClient.post()
			.uri("/tarot/init")
			.body(BodyInserters.fromValue(Map.of("tarots", allTarotsKeyword)))
			.retrieve()
			.bodyToMono(TarotListResponse.class)
			.doOnError(error -> log.error("Fast API CONNECT ERROR: {}",error.getMessage()))
			.onErrorResume(error -> Mono.empty())
			.block();

		if (tarotVectors == null || tarotVectors.getTarots() == null) {
			log.error("Tarot vectors response is null or empty.");
			return;
		}

		tarotVectors.getTarots().forEach(tarotVec -> {
			Tarot tarot = allTarots.get(tarotVec.getId() - 1).setVector(tarotVec.getKeywords());
			deck.put(tarotVec.getId() , tarot.toDto());
		});
	}
}
