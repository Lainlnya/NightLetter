export interface TarotCard {
  cardNo: number;
  cardName: string;
  imgUrl: string;
  cardDir: string;
  cardType: string;
  cardKeyWord: string;
  cardDesc: string;
}

export interface DiaryEntry {
  memberId: number;
  type: string;
  diaryId: number;
  pastCard: TarotCard;
  nowCard: TarotCard;
  futureCard: TarotCard;
  date: string;
  content: string;
  gptComment: string;
}

export interface CardInfo {
  name: string;
  imgUrl: string;
  keyword: string;
  desc: string;
}
