export default async function getTodayCardInfo() {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/diaries/today`,
    {
      method: "GET",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
    }
  );

  return response.json();
}
