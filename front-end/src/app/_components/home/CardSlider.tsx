"use client";

import Image from "next/image";
import styles from "./cardSlider.module.scss";
import tarot_background from "../../../../public/images/tarot-background.webp";
import { parseDateToKoreanFormatWithDay } from "@/utils/dateFormat";
import { motion, useMotionValue } from "framer-motion";
import React, { useEffect, useState, useRef } from "react";
import { useRouter } from "next/navigation";
import useStore from "@/store/date";

const DRAG_BUFFER = 100;

interface CalendarProps {
  isSeen: boolean;
  isClicked: boolean;
  setIsClicked: React.Dispatch<React.SetStateAction<boolean>>;
}

export default function CardSlider({
  isSeen,
  isClicked,
  setIsClicked,
}: CalendarProps) {
  const router = useRouter();
  const [dragging, setDragging] = useState(false);
  const [contents, setContents] = useState([1, 2, 3, 4, 5]);
  const [cardIndex, setCardIndex] = useState(contents.length - 1);
  const [isNotNotedToday, setIsNotNotedToday] = useState(true);
  const { setDate, daysDifference, setDaysDifference } = useStore();

  useEffect(() => {}, []);

  useEffect(() => {
    setDate(parseDateToKoreanFormatWithDay(daysDifference));
  }, [daysDifference]);

  const dragX = useMotionValue(0);

  const onDragStart = () => {
    setDragging(true);
  };

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
  };

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
        {contents.map((_, idx) => {
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
                src={tarot_background}
                className={`${styles.card} ${styles.past}`}
                alt="past_card"
                width={120}
                height={205}
              />
              <Image
                src={tarot_background}
                className={`${styles.card} ${styles.current} `}
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
              {isNotNotedToday && (
                <div className={styles.note_notification}>오늘의 일기 작성</div>
              )}
            </div>
          );
        })}
      </motion.div>
    </div>
  );
}
