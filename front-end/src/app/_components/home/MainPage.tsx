"use client";

import { Suspense, useEffect, useRef, useState } from "react";

import styles from "./mainPage.module.scss";

import useStore from "@/store/date";
import CardSlider from "./CardSlider";
import { Messages } from "@/utils/msg";
import CalendarComponent from "../diaries/Calendar";

import Image from "next/image";
import alarm from "../../../../public/Icons/alarm_icon.svg";
import { isToday, TODAY, TODAY_CONVERTED } from "@/utils/dateFormat";
import { useQuery } from "@tanstack/react-query";

import Loading from "@/app/loading";
import getCardListByPeriod from "@/libs/getCardListByPeriod";

export default function Home() {
  const { date } = useStore();

  const { data } = useQuery({
    queryKey: ['card', 'cards'],
    queryFn: () => getCardListByPeriod('2024-04-01', '2024-05-02')
  });

  const [isSeen, setIsSeen] = useState<boolean>(false);
  const [isClicked, setIsClicked] = useState<boolean>(false);
  const calendarRef = useRef<HTMLDivElement>(null);

  const [isNotedTodayDiaries, SetIsNotedTodayDiaries] = useState(
    isToday(TODAY, data?.[data?.length - 1]?.date)
      ? true
      : false
  );


  useEffect(() => {
    if (data) {
      SetIsNotedTodayDiaries(TODAY_CONVERTED === data?.[data?.length - 1]?.date)
    }
  }, [data]);

  useEffect(() => {
    function handleTouchOutside(e: TouchEvent) {
      if (
        calendarRef.current &&
        !calendarRef.current.contains(e.target as Node)
      ) {
        setIsClicked(true);
        setIsSeen(false);
      }
    }

    document.addEventListener("touchstart", handleTouchOutside);
    return () => {
      document.removeEventListener("touchstart", handleTouchOutside);
    };
  }, [calendarRef, isClicked]);

  return (
    <Suspense fallback={<Loading loadingMessage="불러오는 중 입니다." />}>
      <>
        <header className={styles.header}>
          <div className={styles.header_icons}>
            <Image
              src={alarm}
              alt='alarm'
              width={24}
              height={24}
              className={styles.header_icon}
              onClick={() => setIsSeen(true)}
            />
            {isSeen && (
              <div ref={calendarRef}>
                <CalendarComponent />
              </div>
            )}
          </div>
          <div className={styles.header_title}>
            <h1>반가워요 김남준님.</h1>
            <br />
            <br />
            <br />
            <h1>{date}</h1>
          </div>
        </header>
        <section className={styles.section}>
          <div className={styles.guide}>{Messages.MAIN_PAGE_DRAG_GUIDE}</div>
          <CardSlider
            isSeen={isSeen}
            isClicked={isClicked}
            setIsClicked={setIsClicked}
          />
        </section>
      </>
    </Suspense>
  );
}
