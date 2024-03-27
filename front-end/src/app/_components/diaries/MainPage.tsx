'use client'

import React, { useEffect } from 'react'
import styles from './diaries.module.scss'
import Image from 'next/image'
import back_button from '../../../../public/Icons/back_button.svg'
import alarm from '../../../../public/Icons/calender_icon.svg'
import tarot_background from "../../../../public/images/tarot-background.png";
import { motion, useMotionValue } from "framer-motion";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { DRAG_BUFFER } from '@/utils/animation'
import useStore from '@/store/date'
import { parseDateToKoreanFormatWithDay } from '@/utils/dateFormat'

export default function MainPage() {
    const router = useRouter();
    const [dragging, setDragging] = useState(false);
    const [contents, setContents] = useState([1, 2, 3, 4, 5]);
    const [cardIndex, setCardIndex] = useState(contents.length - 1);
    const { date, setDate, daysDifference, setDaysDifference } = useStore();

    useEffect(() => {
        setDate(parseDateToKoreanFormatWithDay(daysDifference));
    }, [daysDifference]);


    const dragX = useMotionValue(0);

    const onDragStart = () => {
        setDragging(true);
    }

    const onDragEnd = () => {
        setDragging(false);

        const x = dragX.get();

        if (x <= -DRAG_BUFFER && cardIndex < contents.length - 1) {
            setCardIndex((prev) => prev + 1);
            setDaysDifference(daysDifference + 1);
        } else if (x >= DRAG_BUFFER && cardIndex > 0) {
            setCardIndex((prev) => prev - 1);
            setDaysDifference(daysDifference - 1);
        }
    }
    return (
        <div className={styles.root}>
            <header className={styles.header}>
                <Image src={back_button} alt="back_button" className={styles.back_button}
                    onClick={() => { router.replace('/') }}
                />
                <h1>{date}</h1>
                <Image src={alarm} alt="alarm" />
            </header>
            <div
                className={styles.carousel_container}
            >
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
                            <main
                                key={idx}
                                className={styles.diary}
                            >
                                <div className={styles.diary_thumbnail}>
                                    <Image src={tarot_background} className={`${styles.card} ${styles.past}`} alt="past_card" width={120} height={205} />
                                    <Image src={tarot_background} className={`${styles.card} ${styles.current}`} alt="current_card" width={120} height={205} />
                                    <Image src={tarot_background} className={`${styles.card} ${styles.future}`} alt="future_card" width={120} height={205} />
                                </div>
                                <div className={styles.diary_text}>
                                    <h2>오늘의 일기</h2>
                                    <p>나의 눈이 다시 떠진 이유는내 딸 심청이의 슬픈 희생때문이 아니라오직 호날두의 플레이를 보기 위해서였다 나의 눈이 다시 떠진 이유는내 딸 심청이의 슬픈 희생때문이 아니라 오직 호날두의 플레이를 보기 위해서였다 나의 눈이 다시 떠진 이유는내 딸 심청이의 슬픈 희생때문이 아니라 오직 호날두의 플레이를 보기 위해서였다 나의 눈이 다시 </p>
                                </div>
                            </main>
                        );
                    })}
                </motion.div>
            </div>

            <footer className={styles.footer}>
                <p>카드를 좌우로 슬라이드하여 날짜를 이동할 수 있어요.</p>
            </footer>
        </div>
    )
}
