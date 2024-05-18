export default async function checkTodayStatus() {
    const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/diaries/today`, {
        headers: {
            'Content-Type': 'application/json',
        },
        credentials: 'include',
    });

    if (!res.ok) {
        throw new Error("Failed to fetch data");
    }

    return res.json();
}