package com.nightletter.domain.tarot.service;

import java.util.Optional;

import com.nightletter.domain.tarot.dto.TarotDto;

public interface TarotService {
	public Optional<TarotDto> createRandomPastTarot();

	public Optional<TarotDto> getRandomPastTarot();
}
