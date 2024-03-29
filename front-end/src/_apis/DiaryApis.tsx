export async function saveData(newData: any) {
  console.log(newData);
  const response = await fetch(
    "https://dev.letter-for.me/api/v1/diary/diaries",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(newData),
    }
  );

  return response.json();
}
