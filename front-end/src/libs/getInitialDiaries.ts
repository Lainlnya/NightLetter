import { TODAY_CONVERTED } from "@/utils/dateFormat";

export default async function getInitialDiaries(date: string | null = TODAY_CONVERTED) {
    const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/diaries/self`, {
        method: "POST",
        cache: "no-cache",
        headers: {
            'Content-Type': 'application/json',
        },
        credentials: 'include',
        next: {
            tags: ["diary", 'diaries'],
        },
        body: JSON.stringify({
            date: `${date ? date : TODAY_CONVERTED}`,
            direction: "BOTH",
            size: 5
        }),

    });

    if (!res.ok) {
        throw new Error("Failed to fetch data");
    }

    return res.json();
}