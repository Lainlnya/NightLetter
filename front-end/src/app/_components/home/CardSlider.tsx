"use client";

import { Suspense, useEffect, useState, useLayoutEffect, useRef } from "react";

import { ErrorBoundary } from "react-error-boundary";
import { motion, useMotionValue } from "framer-motion";
import { useRouter } from "next/navigation";
import { useQuery } from "@tanstack/react-query";

import Loading from "@/app/loading";
import ErrorFallback from "@/app/_components/error/ErrorFallback";

import { DRAG_BUFFER } from "@/utils/animation";
import { TODAY, convertDateFormatToKorean, isToday, getDateDiff, convertDateFormat } from "@/utils/dateFormat";
import getInitialCards from "@/libs/getInitialCards";
import getPastCardInfo from "@/libs/getPastCardInfo";

import useStore from "@/store/date";

import { CalendarProps } from "@/types/calender";
import { DiaryEntry } from "@/types/card";

import styles from "./cardSlider.module.scss";

import Image from "next/image";
import tarot_background from "../../../../public/images/tarot-background.webp";

export default function CardSlider({ isSeen, isClicked, setIsClicked }: CalendarProps) {
  const { data } = useQuery({ queryKey: ["card", "cards"], queryFn: getInitialCards });

  const router = useRouter();

  const [dragging, setDragging] = useState(false);
  const [cardIndex, setCardIndex] = useState(data?.diaries?.length - 1);
  const [isNotedTodayDiaries, setIsNotedTodayDiaries] = useState(isToday(TODAY, data?.diaries?.[data.diaries.length - 1]?.date) ? true : false);
  const { setDate } = useStore();

  useEffect(() => {
    async function fetchPastCard() {
      try {
        const res = await getPastCardInfo();

        if (!res.ok) {
          return null
        }
        return res.json();
      } catch {
        return null;
      }
    }
    const dateDiff = getDateDiff(convertDateFormat(TODAY), data?.diaries?.[data.diaries.length - 1]?.date);
    if (!data?.diaries?.length && !fetchPastCard()) router.push("/tarot?info=past")
    if (dateDiff > 28 && !fetchPastCard()) router.push("/tarot?info=past")
  }, [])

  useEffect(() => {
    if (data) {
      setCardIndex(data?.diaries?.length - 1);
      setDate(convertDateFormatToKorean(data.diaries?.[cardIndex]?.date));
    }
    setDate(convertDateFormatToKorean(data?.diaries?.[data?.requestDiaryIdx]?.date + 1));
  }, [data])


  useEffect(() => {
    if (data) {
      setDate(convertDateFormatToKorean(data.diaries?.[cardIndex]?.date));
    }
    setDate(convertDateFormatToKorean(data?.diaries?.[cardIndex]?.date));
  }, [cardIndex, data])

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
    <ErrorBoundary FallbackComponent={ErrorFallback}>
      <Suspense fallback={<Loading loadingMessage="로딩중입니다." />}>
        <div className={styles.carousel_container}>
          <motion.div
            initial={{
              translateX: `-${cardIndex * 100}%`,
            }}
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
            {data?.diaries?.map((cardData: DiaryEntry, idx: number) => {
              const { pastCard, nowCard, futureCard } = cardData;

              return (
                <div
                  key={idx}
                  className={styles.card_wrapper}
                  onClick={() => {
                    1
                    if (!isSeen && isClicked) {
                      setIsClicked(false);
                    }

                    if (isClicked === false) router.push("/diaries");
                  }}
                >
                  <Image
                    src={pastCard?.imgUrl ?? tarot_background}
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
                    src={futureCard?.imgUrl ?? tarot_background}
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
      </Suspense>
    </ErrorBoundary>
  );
}
