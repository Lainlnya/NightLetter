'use client';

import Image from "next/image";
import styles from "./mainPage.module.scss";
import alarm from "../../../../public/Icons/alarm_icon.svg";
import calender from "../../../../public/Icons/calender_icon.svg"
import useStore from '@/store/date'
import CardSlider from "./CardSlider";
import { Messages } from "@/utils/msg";

// TODO: 
// 1.서버 데이터 받아서 날짜 동적으로 수정
// 2. 캐러셀 제대로 만들기
// 3. 아이콘 클릭시 캘린더, 알림페이지로 이동하기
// 4. 조건에 따라 알림 띄우기

export default function Home() {
    const { date } = useStore();

    return <>
        <header className={styles.header}>
            <div className={styles.header_icons}>
                <Image src={alarm} alt="alarm" width={24} height={24} className={styles.header_icon} />
                <Image src={calender} alt="calender" width={24} height={24} className={styles.header_icon} />
            </div>
            <div className={styles.header_title}>
                <h1>{date}</h1>
            </div>
        </header>
        <section className={styles.section}>
            <div className={styles.guide}>
                {Messages.MAIN_PAGE_DRAG_GUIDE}
            </div>
            <CardSlider />
        </section>
    </>;
}