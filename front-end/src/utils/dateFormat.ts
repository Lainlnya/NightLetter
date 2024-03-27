export function parseDateToKoreanFormatWithDay(day = 0) {
    const today = new Date();

    if (day !== 0)
        today.setDate(today.getDate() + day);

    return `${today.getFullYear()}년 ${today.getMonth() + 1}월 ${today.getDate()}일`;
}