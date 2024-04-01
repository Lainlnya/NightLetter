package com.nightletter.domain.tarot.service;

import java.util.Optional;

import com.nightletter.domain.diary.dto.EmbedVector;
import com.nightletter.domain.tarot.dto.TarotResponse;
import com.nightletter.domain.tarot.dto.TarotDto;

public interface TarotService {
	Optional<TarotResponse> createRandomPastTarot();
	Optional<TarotResponse> getRandomPastTarot();
	TarotDto findSimilarTarot(EmbedVector diaryEmbedVector);
	TarotResponse findFutureTarot();
}

