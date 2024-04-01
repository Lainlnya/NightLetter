package com.nightletter.domain.tarot.dto;

import com.nightletter.domain.diary.entity.DiaryTarotType;
import com.nightletter.domain.tarot.entity.Tarot;
import com.nightletter.domain.tarot.entity.TarotDirection;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PastTarotResponse {
	String cardName;
	String cardImgUrl;
	DiaryTarotType cardType;
	TarotDirection cardDir;
	String cardKeyWord;
	String cardDesc;

	public static PastTarotResponse of(Tarot tarot, TarotDirection direction) {

		return PastTarotResponse.builder()
			.cardName(tarot.getName())
			.cardImgUrl(tarot.getImgUrl())
			.cardType(DiaryTarotType.PAST)
			.cardDir(direction)
			.cardKeyWord(tarot.getKeyword())
			.cardDesc(tarot.getDescription())
			.build();
	}
}
