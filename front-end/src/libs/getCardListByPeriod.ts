

export default async function getCardListByPeriod(sttDate: string, endDate: string) {
    const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL_V2}/diaries/self`, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
        },
        credentials: 'include',
        next: {
            tags: ["card", 'cards'],
        },
        body: JSON.stringify({
            sttDate: sttDate,
            endDate: endDate
        }),
    });

    if (!res.ok) {
        throw new Error("카드 데이터를 불러오는데 실패했습니다.");
    }

    return res.json();
}