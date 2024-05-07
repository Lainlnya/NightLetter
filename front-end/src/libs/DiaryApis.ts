export async function setData(newData: any) {
  const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/diaries`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    credentials: 'include',
    body: JSON.stringify(newData),
  });

  return response.json();
}

export async function getGPTData() {
  const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/diaries/get_comment`, {
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
  });

  return response.json();
}

export async function getScrapData() {
  const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/diaries/scrap/0`, {
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
  });

  return response.json();
}
