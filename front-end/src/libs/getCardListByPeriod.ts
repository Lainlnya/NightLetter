

export default async function getCardListByPeriod(startDate: string, endDate: string) {
    const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/diaries/self`, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
        },
        credentials: 'include',
        next: {
            tags: ["card", 'cards'],
        },
        body: JSON.stringify({
            startDate: startDate,
            endDate: endDate
        }),
    });

    if (!res.ok) {
        throw new Error("카드 데이터를 불러오는데 실패했습니다.");
    }

    return res.json();
}