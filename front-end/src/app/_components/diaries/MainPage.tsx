"use client";

import React, { useEffect, useLayoutEffect } from "react";
import styles from "./diaries.module.scss";
import Image from "next/image";
import back_button from "../../../../public/Icons/back_button.svg";
import alarm from "../../../../public/Icons/calender_icon.svg";
import tarot_background from "../../../../public/images/tarot-background.png";
import { motion, useMotionValue } from "framer-motion";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { DRAG_BUFFER } from "@/utils/animation";
import useStore from "@/store/date";
import { convertDateFormat, convertDateFormatToKorean, parseDateToKoreanFormatWithDay } from "@/utils/dateFormat";
import { useQuery } from "@tanstack/react-query";
import getInitialDiaries from "@/libs/getInitialDiaries";
import { DiaryEntry } from "@/types/card";

export default function MainPage() {
  const router = useRouter();
  const { date, setDate } = useStore();
  const { data } = useQuery({ queryKey: ["diary", "diaries"], queryFn: () => getInitialDiaries(convertDateFormat(date)) });

  useEffect(() => {

  }, [])

  useEffect(() => {
    setDate(convertDateFormatToKorean(data?.diaries?.[cardIndex]?.date));
  }, [date])

  console.log(data);

  const [dragging, setDragging] = useState(false);
  const [cardIndex, setCardIndex] = useState(data?.diaries?.length - 1);

  const dragX = useMotionValue(0);

  const onDragStart = () => {
    setDragging(true);
  };

  const onDragEnd = () => {
    setDragging(false);

    const x = dragX.get();

    if (x <= -DRAG_BUFFER && cardIndex < data?.diaries?.length - 1) {
      setCardIndex((prev: number) => prev + 1);
      setDate(convertDateFormatToKorean(data?.diaries?.[cardIndex]?.date));
    } else if (x >= DRAG_BUFFER && cardIndex > 0) {
      setCardIndex((prev: number) => prev - 1);
      setDate(convertDateFormatToKorean(data?.diaries?.[cardIndex]?.date));
    }
  };
  return (
    <div className={styles.root}>
      <header className={styles.header}>
        <Image
          src={back_button}
          alt="back_button"
          className={styles.back_button}
          onClick={() => {
            router.replace("/");
          }}
        />
        <h1>{date}</h1>
        <Image src={alarm} alt="alarm" />
      </header>
      <div className={styles.carousel_container}>
        <motion.div
          drag="x"
          initial={{
            translateX: `-${cardIndex * 100}%`,
          }}
          dragConstraints={{
            left: 0,
            right: 0,
          }}
          animate={{
            translateX: `-${cardIndex * 100}%`,
          }}
          style={{
            x: dragX,
          }}
          onDragStart={onDragStart}
          onDragEnd={onDragEnd}
          className={styles.carousel}
        >
          {data?.diaries?.map((value: DiaryEntry, idx: number) => {
            return (
              <main key={idx} className={styles.diary}>
                <div className={styles.diary_thumbnail}>
                  <Image
                    src={tarot_background}
                    className={`${styles.card} ${styles.past}`}
                    alt="past_card"
                    width={120}
                    height={205}
                  />
                  <Image
                    src={tarot_background}
                    className={`${styles.card} ${styles.current}`}
                    alt="current_card"
                    width={120}
                    height={205}
                  />
                  <Image
                    src={tarot_background}
                    className={`${styles.card} ${styles.future}`}
                    alt="future_card"
                    width={120}
                    height={205}
                  />
                </div>
                <div className={styles.diary_text}>
                  <h2>오늘의 일기</h2>
                  <p>
                    나의 눈이 다시 떠진 이유는내 딸 심청이의 슬픈 희생때문이
                    아니라오직 호날두의 플레이를 보기 위해서였다 나의 눈이 다시
                    떠진 이유는내 딸 심청이의 슬픈 희생때문이 아니라 오직
                    호날두의 플레이를 보기 위해서였다 나의 눈이 다시 떠진
                    이유는내 딸 심청이의 슬픈 희생때문이 아니라 오직 호날두의
                    플레이를 보기 위해서였다 나의 눈이 다시{" "}
                  </p>
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
  );
}
