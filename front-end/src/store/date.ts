import { create } from 'zustand';
import { TODAY_CONVERTED } from '@/utils/dateFormat';

interface DateState {
    date: string;
    PIVOT_DATE_YYYY_MM_DD: string;
    daysDifference: number;
    username: string | null;
    setDate: (date: string) => void;
    setDaysDifference: (daysDifference: number) => void;
    setUserName: (username: string) => void;
}

const useStore = create<DateState>(set => ({
    date: "",
    PIVOT_DATE_YYYY_MM_DD: TODAY_CONVERTED,
    username: null,
    daysDifference: 0,
    setDate: (date: string) => set({ date }),
    setDaysDifference: (daysDifference: number) => set({ daysDifference }),
    setUserName: (username: string) => set({ username })
}));

export default useStore;