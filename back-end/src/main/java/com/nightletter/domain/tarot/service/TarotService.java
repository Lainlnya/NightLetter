package com.nightletter.domain.tarot.service;

import java.util.Optional;

import com.nightletter.domain.tarot.dto.PastTarotResponse;

public interface TarotService {
	Optional<PastTarotResponse> createRandomPastTarot();

	Optional<PastTarotResponse> getRandomPastTarot();
}

