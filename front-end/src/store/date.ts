import { create } from 'zustand';
import { parseDateToKoreanFormatWithDay } from '../utils/dateFormat';
import { persist } from 'zustand/middleware';

interface DateState {
    date: string | null;
    daysDifference: number;
    setDate: (date: string) => void;
    setDaysDifference: (daysDifference: number) => void;
}

const useStore = create<DateState>(set => ({
    date: null,
    daysDifference: 0,
    setDate: (date: string) => set({ date }),
    setDaysDifference: (daysDifference: number) => set({ daysDifference })
}));

export default useStore;