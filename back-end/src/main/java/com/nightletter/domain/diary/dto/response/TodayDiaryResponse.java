package com.nightletter.domain.diary.dto.response;

import java.util.List;

import com.nightletter.domain.diary.entity.DiaryTarotType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TodayDiaryResponse {
	private TodayTarot pastCard;
	private TodayTarot nowCard;
	private TodayTarot futureCard;

	public static TodayDiaryResponse of (List<TodayTarot> tarots) {
		TodayDiaryResponse response = new TodayDiaryResponse();

		for (TodayTarot tarot : tarots) {
			if (tarot.getCardType() == DiaryTarotType.PAST) {
				response.setPastCard(tarot);
			}
			else if (tarot.getCardType() == DiaryTarotType.NOW) {
				response.setNowCard(tarot);
			}
			else if (tarot.getCardType() == DiaryTarotType.FUTURE) {
				response.setFutureCard(tarot);
			}
		}

		return response;
	}
}
