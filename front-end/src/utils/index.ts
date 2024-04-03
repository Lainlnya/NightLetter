import { convertDateFormat, convertDateFormatToKorean } from "./dateFormat";

export const getCenterCardIndex = (obj: { diaries: any[]; }, date: string | null) => {
    const converted = convertDateFormatToKorean(date);
    return obj?.diaries?.findIndex(diary => diary.date === converted);
};