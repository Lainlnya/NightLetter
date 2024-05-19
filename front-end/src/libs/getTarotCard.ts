import { CardInfo } from '@/types/card';

export async function getTarotCard(info: string, method: string) {
  const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/tarots/${info}`, {
    method: method,
    headers: {
      'Content-Type': 'application/json',
    },
    credentials: 'include',
  });

  const data = await response.json();
  const { name, imgUrl, keyword, desc }: CardInfo = data;

  return { name, imgUrl, keyword, desc };
}
