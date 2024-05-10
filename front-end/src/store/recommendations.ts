import { ScrapItem } from "@/types/apis";
import { create } from "zustand";
import { devtools, persist } from "zustand/middleware";

interface RecomState {
  stories: ScrapItem[];
  isScrappedStories: (diaryId: number) => boolean;
  toggleStories: (diaryId: number) => void;
  setStories: (recommendDiaries: ScrapItem[]) => void;
}

const useRecomStore = create<RecomState>()(
  devtools(
    persist(
      (set, get) => ({
        stories: [],
        isScrappedStories: (diaryId) => {
          return get().stories.some(
            (story: ScrapItem) => story.diaryId === diaryId && story.isScrapped
          );
        },
        toggleStories: (diaryId) => {
          console.log(diaryId);
          set((state) => ({
            stories: state.stories.map((story) =>
              story.diaryId === diaryId
                ? { ...story, isScrapped: !story.isScrapped }
                : story
            ),
          }));
        },

        setStories: (recommendDiaries: ScrapItem[]) => {
          const initializedDiaries = recommendDiaries.map((diary) => ({
            ...diary,
            isScrapped: false,
          }));
          set(() => ({ stories: initializedDiaries }));
        },
      }),
      {
        name: "RecommendationStories",
      }
    )
  )
);

export default useRecomStore;
