package com.nightletter.domain.tarot.service;

import java.util.Optional;

import com.nightletter.domain.tarot.dto.PastTarotResponse;
import com.nightletter.domain.tarot.dto.TarotDto;

public interface TarotService {
	public Optional<PastTarotResponse> createRandomPastTarot();

	public Optional<PastTarotResponse> getRandomPastTarot();
}
