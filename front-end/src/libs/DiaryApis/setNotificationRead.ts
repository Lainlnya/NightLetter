export async function setNotificationRead(notificationId: number) {
  try {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/notification?notificationId=${notificationId}`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    } else {
      console.log('Success');
    }
  } catch (error) {
    console.error('Error', error);
  }
}
