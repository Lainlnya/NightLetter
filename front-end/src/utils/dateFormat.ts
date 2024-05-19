import dayjs from 'dayjs';
import utc from 'dayjs/plugin/utc';
import timezone from 'dayjs/plugin/timezone';

export function getTodayDate() {
  return dayjs().format('YYYY-MM-DD');
}

export function getPreviousDate(today: string, date: number) {
  return dayjs(today).subtract(date, 'day').format('YYYY-MM-DD');
}

export function getAdditionalDate(today: string, date: number) {
  return dayjs(today).add(date, 'day').format('YYYY-MM-DD');
}

export function parseDateToKoreanFormatWithDay(day = 0) {
  const today = new Date();

  if (day !== 0) today.setDate(today.getDate() + day);

  return `${today.getFullYear()}년 ${today.getMonth() + 1}월 ${today.getDate()}일`;
}

export function convertDateFormat(dateString: string | null) {
  const parts = dateString && dateString.match(/(\d{4})년 (\d{1,2})월 (\d{1,2})일/);

  if (parts) {
    const year = parts[1];
    const month = parts[2].padStart(2, '0');
    const day = parts[3].padStart(2, '0');
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

export const getDateDiff = (d1: string | Date, d2: string | Date) => {
  const date1 = new Date(d1);
  const date2 = new Date(d2);

  const diffDate = date1.getTime() - date2.getTime();
  return Math.abs(diffDate / (1000 * 60 * 60 * 24)); // 밀리초 * 초 * 분 * 시 = 일
};

export const convertTime = (dateString: string | null) => {
  const time = dateString && dateString.split('T')[1];
  const timeSlice = time?.slice(0, 5);
  return timeSlice;
};

export const isToday = (date1: string, data2: string) => date1.match(data2);

export function getNextDate() {
  const tomorrow = new Date();
  tomorrow.setDate(new Date().getDate() + 1);
  tomorrow.setHours(4, 0, 0, 0); // 내일 오전 4시
  return tomorrow;
}

export function formattedDate(dateString: string) {
  const parts = dateString.split('-');
  const month = parts[1];
  const day = parts[2];

  return `${month}월 ${day}일`;
}
export const TODAY = parseDateToKoreanFormatWithDay();
export const TODAY_CONVERTED = getTodayDate();
export const TOMORROW = dayjs().add(1, 'day').format('YYYY-MM-DD');
