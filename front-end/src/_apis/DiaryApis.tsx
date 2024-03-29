export async function saveData(newData: any) {
  console.log(newData);
  const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/diary/diaries`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(newData),
  });

  return response.json();
}
