export interface CardsRequestBody {
  date: string;
  direction: string;
  size: number;
}

export interface ScrapItem {
  diaryId: number;
  nickname: string;
  content: string;
  imgUrl: string;
  scrappedAt: string;
  isScrapped: boolean;
}
