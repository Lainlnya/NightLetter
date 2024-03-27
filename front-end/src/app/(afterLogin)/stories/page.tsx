"use client";

import React from 'react'
import styles from './stories.module.scss'
import { motion, useMotionValue } from "framer-motion";
import { useState } from "react";
import { useRouter } from "next/navigation";

const DRAG_BUFFER = 100;

export default function Diaries() {
    const router = useRouter();
    const [dragging, setDragging] = useState(false);
    const [contents, setContents] = useState([1, 2, 3, 4, 5]);
    const [cardIndex, setCardIndex] = useState(0);

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
    return (
        <div className={styles.root}>
            <header className={styles.header}>
                <p>
                    <span className={styles.nickname}>어이없는 어피치</span>님의 <br /> 사연이 도착했습니다.
                </p>
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
                        translateX: `-${cardIndex * 30.5}rem`
                    }}
                    style={{
                        x: dragX
                    }}
                    onDragStart={onDragStart}
                    onDragEnd={onDragEnd}
                    className={styles.carousel}
                >
                    {contents.map((_, idx) => {
                        // 현재 선택된 카드만 내용을 보여주기 위한 조건
                        const isSelected = idx === cardIndex;
                        return (
                            <main
                                key={idx}
                                className={`${styles.story} ${!isSelected ? styles.inactive : ''}`}
                            >
                                {isSelected && (
                                    <div className={styles.story_contents}>
                                        오늘 바나프레소 신 메뉴를 먹었는데 정말 짜서 입이 오그라들었다. 다신 안 먹어야겠다.
                                        내일은 단 거 먹어야지. 소금라떼 내 인생메뉴
                                    </div>
                                )}
                            </main>
                        );
                    })}
                </motion.div>
            </div>

            <footer className={styles.footer}>
                <p>슬라이드하여 다른 일기를 조회할 수 있어요.</p>
            </footer>
        </div >
    )
}
