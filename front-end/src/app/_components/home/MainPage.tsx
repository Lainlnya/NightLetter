import CardSlider from "./CardSlider";
import Header from "@/app/_components/home/Header";
import { Messages } from "@/utils/msg";
import styles from "./mainPage.module.scss";
import { dehydrate, HydrationBoundary, QueryClient } from "@tanstack/react-query";

// TODO: 
// 1.서버 데이터 받아서 날짜 동적으로 수정
// 2. 캐러셀 제대로 만들기
// 3. 아이콘 클릭시 캘린더, 알림페이지로 이동하기
// 4. 조건에 따라 알림 띄우기

export default async function Home() {
    const queryClient = new QueryClient();
    await queryClient.prefetchQuery({ queryKey: ['posts', 'recommends'], queryFn: () => fetch('/api/posts/recommends').then(res => res.json()) });
    const dehydrateState = dehydrate(queryClient);

    return <main className={styles.root}>
        <Header />
        <section className={styles.section}>
            <div className={styles.guide}>
                {Messages.MAIN_PAGE_DRAG_GUIDE}
            </div>
            <HydrationBoundary state={dehydrateState}>
                <CardSlider />
            </HydrationBoundary>
        </section>
    </main>;
}