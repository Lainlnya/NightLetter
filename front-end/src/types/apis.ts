import { HTMLAttributes } from 'react';

export interface CardsRequestBody {
  date: string;
  direction: string;
  size: number;
}

export interface ScrapItem extends HTMLAttributes<HTMLDivElement> {
  diaryId: number;
  nickname: string;
  content: string;
  imgUrl: string;
  scrappedAt?: string;
  isScrapped: boolean;
}
