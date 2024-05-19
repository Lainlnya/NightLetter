export default async function updateUserNickName(nickname: string) {
  const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/members/nickname?nickname=${nickname}`, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json',
    },
    credentials: 'include',
  });

  if (!res.ok) {
    throw new Error('Failed to fetch data');
  }

  return res.json();
}
