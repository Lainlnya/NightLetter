export default async function getPastCardInfo() {
    const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/tarots/past`, {
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
    });
  
    if (!res.ok) {
      throw new Error('카드 데이터를 불러오는데 실패했습니다.');
    }
  
    return res.json();
  }
  