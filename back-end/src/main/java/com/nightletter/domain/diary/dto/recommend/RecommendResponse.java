package com.nightletter.domain.diary.dto.recommend;

import java.util.List;

import com.nightletter.domain.diary.entity.DiaryTarotType;
import com.nightletter.domain.tarot.entity.Tarot;
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

	public void setCard(Tarot tarot) {
		this.no = tarot.getId() / 2 + tarot.getId() % 2 - 1;
		this.name = tarot.getName();
		this.imgUrl = tarot.getImgUrl();
		this.dir = tarot.getDir();
		this.type = DiaryTarotType.NOW;
		this.keyword = tarot.getKeyword();
		this.desc = tarot.getDescription();
	}
}
