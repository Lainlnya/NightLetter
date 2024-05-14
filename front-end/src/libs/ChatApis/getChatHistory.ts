export default async function getChatHistory(roomId: number) {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/room/${roomId}`,
    {
      method: "GET",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
    }
  );

  if (!response.ok) {
    return [];
  }
  return response.json();
}
