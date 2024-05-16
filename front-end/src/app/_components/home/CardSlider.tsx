"use client";

import { Suspense, useEffect, useState, useLayoutEffect, useRef } from "react";

import { ErrorBoundary } from "react-error-boundary";
import { motion, useMotionValue } from "framer-motion";
import { useRouter } from "next/navigation";
import { useQuery } from "@tanstack/react-query";

import Loading from "@/app/loading";
import ErrorFallback from "@/app/_components/error/ErrorFallback";

import { DRAG_BUFFER } from "@/utils/animation";
import {
  convertDateFormatToKorean,
  getPreviousDate,
} from "@/utils/dateFormat";

import useStore from "@/store/date";

import { CalendarProps } from "@/types/calender";
import { DiaryEntry } from "@/types/card";

import styles from "./cardSlider.module.scss";

import Image from "next/image";
import tarot_background from "../../../../public/images/tarot-background.png";
import getCardListByPeriod from "@/libs/getCardListByPeriod";

export default function CardSlider({
  isSeen,
  isClicked,
  setIsClicked,
}: CalendarProps) {
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

  if (isLoading) return <Loading loadingMessage='로딩중입니다.' />

  const onDragStart = () => {
    setDragging(true);
  };

  const onDragEnd = () => {
    setDragging(false);

    const x = dragX.get();

    if (x <= -DRAG_BUFFER && cardIndex < data?.length - 1) {
      setCardIndex((prev: number) => prev + 1);

      setDate(convertDateFormatToKorean(data?.[cardIndex]?.date));
    } else if (x >= DRAG_BUFFER && cardIndex > 0) {
      setCardIndex((prev: number) => prev - 1);

      setDate(convertDateFormatToKorean(data?.[cardIndex]?.date));
    }
  };

  return (
    <ErrorBoundary FallbackComponent={ErrorFallback}>
      <Suspense fallback={<Loading loadingMessage='로딩중입니다.' />}>
        <div className={styles.carousel_container}>
          <motion.div
            initial={{
              translateX: `-${cardIndex * 100}%`,
            }}
            drag='x'
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
            {data.map((cardData: DiaryEntry, idx: number) => {
              const { pastCard, nowCard, futureCard, content } = cardData;

              return (
                <div
                  key={idx.toString()}
                >
                  <div
                    className={styles.card_wrapper}
                    onClick={() => {
                      if (!isSeen && isClicked) {
                        setIsClicked(false);
                      }
                      // if (isClicked === false) router.push("/diaries");
                    }}
                  >
                    <Image
                      src={pastCard?.imgUrl ?? tarot_background}
                      className={`${styles.card} ${styles.past}`}
                      alt='past_card'
                      width={120}
                      height={205}
                    />
                    <Image
                      src={nowCard?.imgUrl ?? tarot_background}
                      className={`${styles.card} ${styles.current} `}
                      alt='current_card'
                      width={120}
                      height={205}
                    />
                    <Image
                      src={futureCard?.imgUrl ?? tarot_background}
                      className={`${styles.card} ${styles.future}`}
                      alt='future_card'
                      width={120}
                      height={205}
                    />

                  </div>
                  <div
                    className={styles.content_wrapper}
                  >
                    {content ?
                      <div>{content}</div> :
                      <div>이 날은 작성한 일기가 없어요.</div>
                    }
                  </div>

                </div>
              );
            })}
          </motion.div>
        </div>
      </Suspense>
    </ErrorBoundary>
  );
}
