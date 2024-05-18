package com.nightletter.domain.tarot.service;

import java.util.Optional;

import com.nightletter.domain.diary.dto.recommend.EmbedVector;
import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.tarot.dto.TarotResponse;
import com.nightletter.domain.tarot.entity.FutureTarot;
import com.nightletter.domain.tarot.entity.Tarot;
import com.querydsl.core.group.GroupBy;

public interface TarotService {
	Optional<TarotResponse> createRandomPastTarot();

	Optional<TarotResponse> getPastTarot();

	Optional<TarotResponse> getNowTarot();

	Tarot findSimilarTarot(EmbedVector diaryEmbedVector);

	TarotResponse findFutureTarot();

	Tarot makeRandomTarot(int... ignoreTarotsId);

	Optional<Tarot> findPastTarot();

	Optional<FutureTarot> getFutureTarot();

	Optional<FutureTarot>  updateWithNewEntity();
	Optional<FutureTarot>  updateOnlyFlipped(Integer memberId);

}

