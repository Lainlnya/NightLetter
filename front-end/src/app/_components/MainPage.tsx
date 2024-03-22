'use client';

import Image from "next/image";
import styles from "./mainPage.module.scss";
import alarm from "../../../public/Icons/alarm_icon.svg";
import calender from "../../../public/Icons/calender_icon.svg"
import tarot_background from "../../../public/images/tarot-background.png";
import { motion, useMotionValue } from "framer-motion";
import { useState } from "react";
import { useRouter } from "next/navigation";

// TODO: 
// 1.서버 데이터 받아서 날짜 동적으로 수정
// 2. 캐러셀 제대로 만들기
// 3. 아이콘 클릭시 캘린더, 알림페이지로 이동하기
// 4. 조건에 따라 알림 띄우기
const DRAG_BUFFER = 100;

export default function Home() {
    const router = useRouter();
    const [dragging, setDragging] = useState(false);
    const [contents, setContents] = useState([1, 2, 3, 4, 5]);
    const [cardIndex, setCardIndex] = useState(contents.length - 1);

    const dragX = useMotionValue(0);

    const onDragStart = () => {
        setDragging(true);
    }

    const onDragEnd = () => {
        setDragging(false);

        const x = dragX.get();

        if (x <= -DRAG_BUFFER && cardIndex < contents.length - 1) {
            setCardIndex((prev) => prev + 1);
        } else if (x >= DRAG_BUFFER && cardIndex > 0) {
            setCardIndex((prev) => prev - 1);
        }
    }

    return <>
        <header className={styles.header}>
            <div className={styles.header_icons}>
                <Image src={alarm} alt="alarm" width={24} height={24} className={styles.header_icon} />
                <Image src={calender} alt="calender" width={24} height={24} className={styles.header_icon} />
            </div>
            <div className={styles.header_title}>
                <h1>2024년 3월 11일</h1>
            </div>
        </header>

        <section className={styles.section}>
            <div className={styles.guide}>
                카드를 슬라이드하여 날짜를 바꿀 수 있어요.
            </div>
            <div className={
                styles.carousel_container
            }>
                <motion.div
                    drag="x"
                    dragConstraints={{
                        left: 0,
                        right: 0
                    }}
                    animate={{
                        translateX: `-${cardIndex * 100}%`
                    }}
                    style={{
                        x: dragX
                    }}
                    onDragStart={onDragStart}
                    onDragEnd={onDragEnd}
                    className={styles.carousel}
                >
                    {contents.map((_, idx) => {
                        return (
                            <div
                                key={idx}
                                className={styles.card_wrapper}
                                onClick={() => router.push('/diaries')}
                            >

                                <Image src={tarot_background} className={`${styles.card} ${styles.past}`} alt="past_card" width={120} height={205} />
                                <Image src={tarot_background} className={`${styles.card} ${styles.current}`} alt="current_card" width={120} height={205} />
                                <Image src={tarot_background} className={`${styles.card} ${styles.future}`} alt="future_card" width={120} height={205} />
                            </div>
                        );
                    })}
                </motion.div>
            </div>
        </section>
    </>;
}