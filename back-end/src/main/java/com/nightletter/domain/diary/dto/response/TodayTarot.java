package com.nightletter.domain.diary.dto.response;

import com.nightletter.domain.diary.entity.DiaryTarotType;
import com.nightletter.domain.tarot.entity.TarotDirection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TodayTarot {
	private Integer cardNo;
	private String cardName;
	private String cardImgUrl;
	private TarotDirection cardDir;
	private DiaryTarotType cardType;

}
