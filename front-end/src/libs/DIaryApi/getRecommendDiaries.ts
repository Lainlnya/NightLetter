export async function getRecommendDiaries() {
  const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/diaries/recommend`, {
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
  });

  return response.json();
}
