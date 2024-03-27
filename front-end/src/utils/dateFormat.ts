export function parseDateToKoreanFormatWithDay(day = 0) {
    const today = new Date();

    if (day !== 0)
        today.setDate(today.getDate() + day);

    return `${today.getFullYear()}년 ${today.getMonth() + 1}월 ${today.getDate()}일`;
}
export function convertDateFormat(dateString: string) {
    // "YYYY년 mm월 dd일" 형태의 문자열을 "YYYY", "mm", "dd"로 분리합니다.
    const parts = dateString.match(/(\d{4})년 (\d{1,2})월 (\d{1,2})일/);

    // 분리된 각 부분을 'YYYY-mm-dd' 형식으로 조합합니다.
    // parts[0]은 전체 일치 문자열이므로, parts[1]부터 사용합니다.
    // 일이 한 자리 숫자일 경우 앞에 0을 붙여 두 자리로 만듭니다.
    // 월은 한 자리일 때 그대로 사용합니다.
    if (parts) {
        const year = parts[1];
        const month = parts[2]; // 월은 한 자리일 때 변경 없음
        const day = parts[3];
        return `${year}-${month}-${day}`;
    } else {
        // 일치하는 형식이 없을 경우 오류 메시지를 반환합니다.
        return '입력 형식이 올바르지 않습니다.';
    }
}
