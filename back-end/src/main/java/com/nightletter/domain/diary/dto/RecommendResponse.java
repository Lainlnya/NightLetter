package com.nightletter.domain.diary.dto;

import java.util.List;

import com.nightletter.domain.diary.entity.DiaryTarotType;
import com.nightletter.domain.tarot.dto.TarotDto;
import com.nightletter.domain.tarot.entity.TarotDirection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecommendResponse {

	private int cardNo;
	private String cardName;
	private String cardImgUrl;
	private TarotDirection cardDir;
	private DiaryTarotType cardType;
	private String cardKeyWord;
	private String cardDesc;
	private List<RecommendDiaryResponse> recommendDiaries;

	public void setCard(TarotDto tarot) {
		this.cardNo = tarot.id()/2 + tarot.id()%2 - 1;
		this.cardName = tarot.name();
		this.cardImgUrl = tarot.imgUrl();
		this.cardDir = tarot.dir();
		this.cardType = DiaryTarotType.NOW;
		this.cardKeyWord = tarot.keyword();
		this.cardDesc = tarot.description();
	}
}
