export async function getGPTData() {
  const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/diaries/get_comment`, {
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
  });

  return response.json();
}
