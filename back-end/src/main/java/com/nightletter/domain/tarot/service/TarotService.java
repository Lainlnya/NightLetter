package com.nightletter.domain.tarot.service;

import java.util.Optional;

import com.nightletter.domain.diary.dto.recommend.EmbedVector;
import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.tarot.dto.TarotResponse;
import com.nightletter.domain.tarot.entity.Tarot;

public interface TarotService {
	Optional<TarotResponse> createRandomPastTarot();
	Optional<TarotResponse> getRandomPastTarot();
	Tarot findSimilarTarot(EmbedVector diaryEmbedVector);
	TarotResponse findFutureTarot();
	Tarot makeRandomTarot(int... ignoreTarotsId);
	Tarot findPastTarot(Member currentMember);
}

