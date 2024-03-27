import assets from "./assets.json";

interface CardInfo {
  no: number;
  name: string;
  right: string;
  reverse: string;
  description: string;
}

export function getTarotCard(): CardInfo | undefined {
  const randomNum: number = Math.floor(Math.random() * 78);
  const card: CardInfo = assets[randomNum];
  return card;
}
