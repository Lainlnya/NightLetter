export async function setScrapData(diaryId: number) {
  try {
    const response = await fetch(
      `${process.env.NEXT_PUBLIC_API_URL}/diaries/scrap?diaryId=${diaryId}`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
      }
    );

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    } else {
      console.log("Success");
    }
  } catch (error) {
    console.error("Error", error);
  }
}

export async function deleteScrapData(diaryId: number) {
  try {
    const response = await fetch(
      `${process.env.NEXT_PUBLIC_API_URL}/diaries/scrap?diaryId=${diaryId}`,
      {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
      }
    );

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    } else console.log("Deleted");
  } catch (error) {
    console.error("Error", error);
  }
}
