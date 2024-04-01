interface CardInfo {
  cardNo: number;
  cardName: string;
  cardImgUrl: string;
  cardKeyWord: string;
  cardDesc: string;
}

export async function getTarotCard(info: string) {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/tarots/${info}`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    }
  );

  const data = await response.json();
  const { cardNo, cardName, cardImgUrl, cardKeyWord, cardDesc }: CardInfo =
    data;

  return { cardNo, cardName, cardImgUrl, cardKeyWord, cardDesc };
}
