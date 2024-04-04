export default async function getPastCardInfo() {
    const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/tarots/past`, {
        cache: "no-cache",
        headers: {
            'Content-Type': 'application/json',
        },
        credentials: 'include',
    });

    if (!res.ok) {
        return null;
    }

    return res.json();
}