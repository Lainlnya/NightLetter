"use client";

import Image from "next/image";
import styles from "./cardSlider.module.scss";
import tarot_background from "../../../../public/images/tarot-background.png";
import { parseDateToKoreanFormatWithDay } from "@/utils/dateFormat";
import { motion, useMotionValue } from "framer-motion";
import React, { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { DRAG_BUFFER } from "@/utils/animation";
import getInitialCards from "@/libs/getInitialCards";
import { useQuery } from "@tanstack/react-query";
import useStore from "@/store/date";
import {CalendarProps} from "@/types/calender";
import {DiaryEntry} from "@/types/card";
import { TODAY } from "@/utils/dateFormat";

export default function CardSlider({
  isSeen,
  isClicked,
  setIsClicked,
}: CalendarProps) {
  const { data, isLoading, isError } = useQuery({queryKey : ["card", "cards"], queryFn: getInitialCards});

  const router = useRouter();
  const [dragging, setDragging] = useState(false);
  const [cardIndex, setCardIndex] = useState(data?.diaries?.length - 1);
  const { setDate } = useStore();
  
  const dragX = useMotionValue(0);

  const onDragStart = () => {
    setDragging(true);
  };

  const onDragEnd = () => {
    setDragging(false);

    const x = dragX.get();

    if (x <= -DRAG_BUFFER && cardIndex < data?.diaries?.length - 1) {
      setCardIndex((prev) => prev + 1);
      setDate(data?.diaries?.[cardIndex]?.date);
    } else if (x >= DRAG_BUFFER && cardIndex > 0) {
      setCardIndex((prev) => prev - 1);
      setDate(data?.diaries?.[cardIndex]?.date);
    }
  };

  if (isLoading) {
    return (
      <div>
        <>로딩중입니다.</>
      </div>
    );
  }

  if (isError) {
    return (
      <>에러발생</>
    );
  }

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
        {data?.diaries?.map((cardData : DiaryEntry, idx : number) => {
          // 나중에 과거, 미래로 교체
          const {pastCard, nowCard, futureCard} = cardData;

          return (
            <div
              key={idx}
              className={styles.card_wrapper}
              onClick={() => {
                if (!isSeen && isClicked) {
                  setIsClicked(false);
                }

                if (isClicked === false) router.push("/diaries");
              }}
            >
              <Image
                src={ nowCard?.imgUrl ?? tarot_background}
                className={`${styles.card} ${styles.past}`}
                alt="past_card"
                width={120}
                height={205}
              />
              <Image
                src={nowCard?.imgUrl ?? tarot_background}
                className={`${styles.card} ${styles.current} `}
                alt="current_card"
                width={120}
                height={205}
              />
              <Image
                src={nowCard?.imgUrl ?? tarot_background}
                className={`${styles.card} ${styles.future}`}
                alt="future_card"
                width={120}
                height={205}
              />
            </div>
          );
        })}

      </motion.div>
    </div>
  );
}
