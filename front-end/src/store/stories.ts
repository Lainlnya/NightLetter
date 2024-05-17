import { ScrapItem } from "@/types/apis";
import { create } from "zustand";

interface ScrapState {
  scraps: ScrapItem[];
  page: number;
  hasMore: boolean;
  isScrappedDiary: (diaryId: number) => boolean;
  addScrap: (scrap: ScrapItem) => void;
  removeScrap: (diaryId: number) => void;
  toggleScrap: (diaryId: number) => void;
  loadScraps: () => Promise<void>;
}

const useScrapStore = create<ScrapState>((set, get) => ({
  scraps: [],
  page: 0,
  hasMore: true,
  isScrappedDiary: (diaryId) => {
    return get().scraps.some(
      (scrap: ScrapItem) => scrap.diaryId === diaryId && scrap.isScrapped
    );
  },

  addScrap: (scrap) =>
    set((state) => ({
      scraps: [...state.scraps, scrap],
    })),

  removeScrap: (diaryId) =>
    set((state) => ({
      scraps: state.scraps.filter((scrap) => scrap.diaryId !== diaryId),
    })),

  toggleScrap: (diaryId) =>
    set((state) => ({
      scraps: state.scraps.map((scrap) =>
        scrap.diaryId === diaryId
          ? { ...scrap, isScrapped: !scrap.isScrapped }
          : scrap
      ),
    })),

  loadScraps: async () => {
    const { page, scraps, hasMore } = get();
    if (!hasMore) return;
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/diaries/scrap?page=${page}`,
        {
          method: "GET",
          headers: { "Content-Type": "application/json" },
          credentials: "include",
        }
      );

      if (response.ok) {
        const data = await response.json();
        console.log(data);
        const scrapItems = data.content.map((value: ScrapItem) => ({
          ...value,
          isScrapped: true,
        }));
        set({
          scraps: [...scraps, ...scrapItems],
          page: page + 1,
          hasMore: data.content.length === 10,
        });
      }
    } catch (error) {
      console.error("Failed to load more scraps", error);
    }
  },
}));

export default useScrapStore;
