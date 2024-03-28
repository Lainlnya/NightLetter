package com.nightletter.domain.diary.dto;

import java.util.ArrayList;
import java.util.List;

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
	private List<RecommendDiaryResponse> recommendDiaries;

	public static DiaryCreateResponse createTemp(){
		DiaryCreateResponse temp = new DiaryCreateResponse();
		temp.cardNo = 1;
		temp.cardName = "광대 The Fool";
		temp.cardImgUrl = "https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg";
		temp.cardDir = TarotDirection.FORWARD;
		temp.cardType = DiaryTarotType.NOW;
		temp.cardKeyWord = "진짜, 집에, 가고, 싶다";
		temp.cardDesc = "이 카드는 정말 집에 가고 싶다는 카드입니다.";
		temp.recommendDiaries = new ArrayList<>();
		temp.recommendDiaries.add(new RecommendDiaryResponse("김영섭", "나도 집에 가고 싶단다. "));
		temp.recommendDiaries.add(new RecommendDiaryResponse("김효진", "보헤미안 김효진"));
		temp.recommendDiaries.add(new RecommendDiaryResponse("김남준", "머리 이쁘지"));
		temp.recommendDiaries.add(new RecommendDiaryResponse("이승헌", "내가 어떤 이승헌이게"));
		temp.recommendDiaries.add(new RecommendDiaryResponse("김예림", "예림이 그 패 봐바"));
		temp.recommendDiaries.add(new RecommendDiaryResponse("이승헌", "아 집 가고 싶다"));
		temp.recommendDiaries.add(new RecommendDiaryResponse("아이유", "얼마 전에 콘서트를 열었다. 정말 성공적이여서 너무 기쁜 하루였다."));
		temp.recommendDiaries.add(new RecommendDiaryResponse("손흥민", "봉준호 BTS 손흥민 전부 다 월드클래스라고 생각합니다. 앞으로 더 열심히 할게요."));
		temp.recommendDiaries.add(new RecommendDiaryResponse("마동석", "곧 범죄도시4가 개봉합니다. 많은 사랑과 관심은 저에게 힘이 됩니다."));
		temp.recommendDiaries.add(new RecommendDiaryResponse("김성욱", "입실 시간이에요. 다들 오늘도 화이팅 하세요!"));

		return temp;
	}

}
