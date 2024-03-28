package com.nightletter.domain.tarot.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.nightletter.domain.tarot.dto.TarotDto;
import com.nightletter.domain.tarot.dto.TarotListResponse;
import com.nightletter.domain.tarot.entity.Tarot;
import com.nightletter.domain.tarot.repository.TarotRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TarotService {

	private static final Map<Integer, Tarot> Deck = new HashMap<>();
	private final TarotRepository tarotRepository;
	private final WebClient webClient;

	@PostConstruct
	private void getTarotEmbedded() {
		List<TarotDto> allTarots = tarotRepository.getAllTarots();

		TarotListResponse tarots = webClient.post()
			.uri("/tarot/init")
			.body(BodyInserters.fromValue(Map.of("tarots", allTarots)))
			.retrieve()
			.bodyToMono(TarotListResponse.class)
			.block();

		tarots.getTarots().forEach(tarotResponse ->
			Deck.put(tarotResponse.getId(),
				new Tarot(allTarots.get(tarotResponse.getId() - 1), tarotResponse.getVector()
				)));
	}
}
