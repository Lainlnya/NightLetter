package com.nightletter.domain.diary.dto.response;

import com.nightletter.domain.tarot.entity.TarotDirection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FutureTarotResponse {
	String futureTarotName;
	TarotDirection futureTarotDir;
	String diaryContent;
}
