import { convertDateFormatToKorean, getTodayDate } from '@/utils/dateFormat';
import { create } from 'zustand';

interface DateState {
    date: string;
    daysDifference: number;
    username: string | null;
    setDate: (date: string) => void;
    setDaysDifference: (daysDifference: number) => void;
    setUserName: (username: string) => void;
}

const useStore = create<DateState>(set => ({
    date: "",
    username: null,
    daysDifference: 0,
    setDate: (date: string) => set({ date }),
    setDaysDifference: (daysDifference: number) => set({ daysDifference }),
    setUserName: (username: string) => set({ username })
}));

export default useStore;