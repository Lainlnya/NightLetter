export async function getNotifications() {
  const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/notification`, {
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
  });

  if (response.ok) {
    console.log(response);
  }

  return response.json();
}
