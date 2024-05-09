import { ScrapItem } from '@/types/apis';
import { create } from 'zustand';
import { devtools, persist } from 'zustand/middleware';

interface RecomState {
  stories: ScrapItem[];
  toggleStories: (diaryId: number) => void;
  setStories: (recommendDiaries: ScrapItem[]) => void;
}

const useRecomStore = create<RecomState>()(
  devtools(
    persist(
      (set) => ({
        stories: [],
        toggleStories: (diaryId) => {
          console.log(diaryId);
          set((state) => ({
            stories: state.stories.map((story) =>
              story.diaryId === diaryId ? { ...story, isScrapped: !story.isScrapped } : story
            ),
          }));
        },
        setStories: (recommendDiaries: ScrapItem[]) => {
          const initializedDiaries = recommendDiaries.map((diary) => ({
            ...diary,
            isScrapped: false,
          }));
          // 값 덮어쓰기를 위한 파라미터 true 추가
          set(() => ({ stories: initializedDiaries }));
        },
      }),
      {
        name: 'RecommendationStories',
      }
    )
  )
);

export default useRecomStore;
