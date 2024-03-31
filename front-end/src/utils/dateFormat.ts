export function parseDateToKoreanFormatWithDay(day = 0) {
    const today = new Date();

    if (day !== 0)
        today.setDate(today.getDate() + day);

    return `${today.getFullYear()}년 ${today.getMonth() + 1}월 ${today.getDate()}일`;
}
function convertDateFormat(dateString : string) {
    const parts = dateString.match(/(\d{4})년 (\d{1,2})월 (\d{1,2})일/);
  
    if (parts) {
      const year = parts[1];
      const month = parts[2].padStart(2, '0');
      const day = parts[3].padStart(2, '0');
      return `${year}-${month}-${day}`;
    } else {
      return '입력 형식이 올바르지 않습니다.';
    }
  }

export function getTodayDate() {
    var d = new Date();
    return d.getFullYear() + "-" + ((d.getMonth() + 1) > 9 ? (d.getMonth() + 1).toString() : "0" + (d.getMonth() + 1)) + "-" + (d.getDate() > 9 ? d.getDate().toString() : "0" + d.getDate().toString());
}

export const TODAY = parseDateToKoreanFormatWithDay();
