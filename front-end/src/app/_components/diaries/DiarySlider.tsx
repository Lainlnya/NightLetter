"use client";

import React, { useEffect } from "react";
import styles from "./diarySlider.module.scss";
import Image from "next/image";

import tarot_background from "../../../../public/images/tarot-background.png";
import { motion, useMotionValue } from "framer-motion";
import { useState } from "react";
import { DRAG_BUFFER } from "@/utils/animation";
import useStore from "@/store/date";
import { convertDateFormatToKorean, getPreviousDate, parseDateToKoreanFormatWithDay } from "@/utils/dateFormat";
import { useRouter } from "next/navigation";
import getCardListByPeriod from "@/libs/getCardListByPeriod";
import { useQuery } from "@tanstack/react-query";
import Loading from "@/app/loading";

export default function DiarySlider() {
  const { PIVOT_DATE_YYYY_MM_DD, setDate } = useStore();

  const { data, isLoading } = useQuery({
    queryKey: ['card', PIVOT_DATE_YYYY_MM_DD],
    queryFn: () => getCardListByPeriod(getPreviousDate(PIVOT_DATE_YYYY_MM_DD, 30), PIVOT_DATE_YYYY_MM_DD),
  });

  const router = useRouter();

  const [dragging, setDragging] = useState(false);
  const [cardIndex, setCardIndex] = useState(data?.length - 1);

  useEffect(() => {
    if (data) {
      setCardIndex(data?.length - 1);
      setDate(convertDateFormatToKorean(data?.[cardIndex]?.date));
    }
    setDate(
      convertDateFormatToKorean(
        data?.[data?.requestDiaryIdx]?.date + 1
      )
    );
  }, [data]);

  useEffect(() => {
    if (data) {
      setDate(convertDateFormatToKorean(data?.[cardIndex]?.date));
    }
    setDate(convertDateFormatToKorean(data?.[cardIndex]?.date));
  }, [cardIndex, data]);

  const dragX = useMotionValue(0);

  const onDragStart = () => {
    setDragging(true);
  };

  const onDragEnd = () => {
    setDragging(false);

    const x = dragX.get();

    if (x <= -DRAG_BUFFER && cardIndex < data?.length - 1) {
      setCardIndex((prev) => prev + 1);
    } else if (x >= DRAG_BUFFER && cardIndex > 0) {
      setCardIndex((prev) => prev - 1);
    }
  };

  if (isLoading) return <Loading loadingMessage='로딩중입니다.' />

  return (
    <div className={styles.carousel_container}>
      <motion.div
        drag="x"
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
        {/* {data?.map((_:, idx) => {
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
                  떠진 이유는내 딸 심청이의 슬픈 희생때문이 아니라 오직 호날두의
                  플레이를 보기 위해서였다 나의 눈이 다시 떠진 이유는내 딸
                  심청이의 슬픈 희생때문이 아니라 오직 호날두의 플레이를 보기
                  위해서였다 나의 눈이 다시{" "}
                </p>
              </div>
            </main>
          );
        })} */}
      </motion.div>
    </div>
  );
}
