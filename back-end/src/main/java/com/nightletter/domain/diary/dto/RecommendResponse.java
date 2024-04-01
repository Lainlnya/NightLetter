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

	private int no;
	private String name;
	private String imgUrl;
	private TarotDirection dir;
	private DiaryTarotType type;
	private String keyword;
	private String desc;
	private List<RecommendDiaryResponse> recommendDiaries;

	public void setCard(TarotDto tarot) {
		this.no = tarot.id() / 2 + tarot.id() % 2 - 1;
		this.name = tarot.name();
		this.imgUrl = tarot.imgUrl();
		this.dir = tarot.dir();
		this.type = DiaryTarotType.NOW;
		this.keyword = tarot.keyword();
		this.desc = tarot.description();
	}
}
