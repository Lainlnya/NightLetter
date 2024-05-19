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


export interface NullableTarotDiary { 
  writerId: number | null;
  type: string | null;
  diaryId: number | null;
  pastCard: {
    id: number | null;
    name: string | null;
    imgUrl: string | null;
    dir: string | null;
    keyword: string | null;
    description: string | null;
    embedVector: string | null;
  } | null;
  nowCard: {
    id: number | null;
    name: string | null;
    imgUrl: string | null;
    dir: string | null;
    keyword: string | null;
    description: string | null;
    embedVector: string | null;
  } | null;
  futureCard: {
    id: number | null;
    name: string | null;
    imgUrl: string | null;
    dir: string | null;
    keyword: string | null;
    description: string | null;
    embedVector: string | null;
  } | null;
  date: string;
  content: string | null;
  gptComment: string | null;
}