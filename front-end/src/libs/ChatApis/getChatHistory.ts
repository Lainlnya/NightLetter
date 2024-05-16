export default async function getChatHistory(roomId: string, pageNo: number) {
  const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/chat?roomId=${roomId}&pageNo=${pageNo}`, {
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
  });

  if (!response.ok) {
    throw new Error('Network response was not ok');
  }

  const data = await response.json();
  console.log(data.content);
  return {
    data: data.content, // 메시지 배열
    nextPage: data.nextPage, // 다음 페이지 번호 (필요시)
  };
}
