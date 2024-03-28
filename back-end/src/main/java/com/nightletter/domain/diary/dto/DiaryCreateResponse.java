package com.nightletter.domain.diary.dto;

import com.nightletter.domain.diary.entity.DiaryTarotType;
import com.nightletter.domain.tarot.entity.TarotDirection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DiaryCreateResponse {

	private int cardNo;
	private String cardName;
	private String cardImgUrl;
	private TarotDirection cardDir;
	private DiaryTarotType cardType;
	private String cardKeyWord;
	private String cardDesc;


	public static DiaryCreateResponse createTemp(){
		DiaryCreateResponse temp = new DiaryCreateResponse();
		temp.cardNo = 1;
		temp.cardName = "광대 The Fool";
		temp.cardImgUrl = "https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg";
		temp.cardDir = TarotDirection.FORWARD;
		temp.cardType = DiaryTarotType.NOW;
		temp.cardKeyWord = "진짜, 집에, 가고, 싶다";
		temp.cardDesc = "이 카드는 정말 집에 가고 싶다는 카드입니다.";
		return temp;
	}

}
