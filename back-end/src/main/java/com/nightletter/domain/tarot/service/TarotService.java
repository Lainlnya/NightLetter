package com.nightletter.domain.tarot.service;

import java.util.Optional;

import com.nightletter.domain.diary.dto.EmbedVector;
import com.nightletter.domain.tarot.dto.PastTarotResponse;
import com.nightletter.domain.tarot.dto.TarotDto;

public interface TarotService {
	Optional<PastTarotResponse> createRandomPastTarot();
	Optional<PastTarotResponse> getRandomPastTarot();
	TarotDto findSimilarTarot(EmbedVector diaryEmbedVector);


}

