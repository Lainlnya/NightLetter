import { getTodayDate } from "@/utils/dateFormat";

export default async function getInitialCards() {
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
            date: getTodayDate(),
            direction: "BEFORE",
            size: 1
        }),

    });

    if (!res.ok) {
        throw new Error("Failed to fetch data");
    }

    return res.json();
}