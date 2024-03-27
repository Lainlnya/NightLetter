import CardSlider from "./CardSlider";
import Header from "@/app/_components/home/Header";
import { Messages } from "@/utils/msg";
import RQProvider from "../RQProvider";
import styles from "./mainPage.module.scss";

// TODO: 
// 1.서버 데이터 받아서 날짜 동적으로 수정
// 2. 캐러셀 제대로 만들기
// 3. 아이콘 클릭시 캘린더, 알림페이지로 이동하기
// 4. 조건에 따라 알림 띄우기

export default function Home() {
    return <main className={styles.root}>
        <Header />
        <section className={styles.section}>
            <div className={styles.guide}>
                {Messages.MAIN_PAGE_DRAG_GUIDE}
            </div>
            <RQProvider>
                <CardSlider />
            </RQProvider>
        </section>
    </main>;
}