export default async function getUserNickName() {
    const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/members/nickname`, {
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