export async function setData(newData: any) {
  const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/diaries`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
    body: JSON.stringify(newData),
  });

  return response.json();
}
