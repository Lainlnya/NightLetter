export function parseDateToKoreanFormatWithDay(day = 0) {
  const today = new Date();

  if (day !== 0) today.setDate(today.getDate() + day);

  return `${today.getFullYear()}년 ${today.getMonth() + 1
    }월 ${today.getDate()}일`;
}

export function convertDateFormat(dateString: string | null) {
  const parts =
    dateString && dateString.match(/(\d{4})년 (\d{1,2})월 (\d{1,2})일/);

  if (parts) {
    const year = parts[1];
    const month = parts[2].padStart(2, "0");
    const day = parts[3].padStart(2, "0");
    return `${year}-${month}-${day}`;
  } else {
    return '';
  }
}

export function convertDateFormatToKorean(dateString: string) {

  const parts = dateString && dateString.match(/(\d{4})-(\d{1,2})-(\d{1,2})/);

  if (parts) {
    const year = parts[1];
    const month = parts[2];
    const day = parts[3];

    return `${year}년 ${month}월 ${day}일`;
  } else {
    return '';
  }
}

export function getTodayDate() {
  var d = new Date();
  return (
    d.getFullYear() +
    "-" +
    (d.getMonth() + 1 > 9
      ? (d.getMonth() + 1).toString()
      : "0" + (d.getMonth() + 1)) +
    "-" +
    (d.getDate() > 9 ? d.getDate().toString() : "0" + d.getDate().toString())
  );
}

export const getDateDiff = (d1: string | Date, d2: string | Date) => {
  const date1 = new Date(d1);
  const date2 = new Date(d2);


  const diffDate = date1.getTime() - date2.getTime();
  return Math.abs(diffDate / (1000 * 60 * 60 * 24)); // 밀리초 * 초 * 분 * 시 = 일
};

export const isToday = (date1: string, data2: string) => date1.match(data2);

export const TODAY = parseDateToKoreanFormatWithDay();
export const TODAY_CONVERTED = convertDateFormat(TODAY);

export function getNextDate() {
  const tomorrow = new Date();
  tomorrow.setDate(new Date().getDate() + 1);
  tomorrow.setHours(4, 0, 0, 0); // 내일 오전 4시
  return tomorrow;
}
export const TOMORROW = getNextDate();
