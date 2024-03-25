import { create } from 'zustand';

interface DateState {
    date: string;
    setDate: (date: string) => void;
}

export const useDateStore = create<DateState>((set) => ({
    date: new Date().toISOString(),
    setDate: (date: string) => set({ date }),
}));