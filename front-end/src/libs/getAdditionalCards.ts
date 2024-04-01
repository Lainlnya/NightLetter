import { CardsRequestBody } from "../types/apis";

export default async function getAdditionalCards({ date, direction, size }: CardsRequestBody) {
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
            date: date,
            direction: direction,
            size: size
        }),

    });

    if (!res.ok) {
        throw new Error("Failed to fetch data");
    }

    return res.json();
}