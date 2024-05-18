import { NullableTarotDiary } from "./card";

export interface DataProps {
    isSeen: boolean;
    isClicked: boolean;
    setIsClicked: React.Dispatch<React.SetStateAction<boolean>>;
    data: NullableTarotDiary[];
    cardIndex: number;
    setCardIndex: React.Dispatch<React.SetStateAction<number>>;
}
  