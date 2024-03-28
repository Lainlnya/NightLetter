export async function saveData(newData) {
  console.log(newData);
  const response = await fetch("https://letter-for.me/api/test/diary/diaries", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(newData),
  });

  return response.json();
}
