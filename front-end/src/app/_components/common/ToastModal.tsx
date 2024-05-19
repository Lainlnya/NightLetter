'use client'

import styles from '@/app/_components/common/toastModal.module.scss';
import { useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';
import checkTodayStatus from '@/libs/checkTodayStatus';
import getPastCardInfo from '@/libs/getPastCardInfo';

const ToastModal = () => {
  const router = useRouter();
  const [todayStatus, setTodayStatus] = useState("");

  useEffect(() => {
    async function fetchData() {
      try {
        const data = await checkTodayStatus();
        console.log(data);
        const pastCardInfo = await getPastCardInfo();
        console.log(pastCardInfo);
        const { pastCard, nowCard, futureCard } = await checkTodayStatus();
        if (pastCard && nowCard && futureCard) setTodayStatus("complete");
        if (pastCard && nowCard && !futureCard) setTodayStatus("now");
        if (pastCard && !nowCard && !futureCard) setTodayStatus("past");
      } catch (error) {
        setTodayStatus("not checked");
      }
    } 
    fetchData();
  },[])
 
  return (
    todayStatus !== "complete" && todayStatus !== "" && 
    <div className={styles.toast_container}>
      <div>
        {todayStatus === "not checked" && 
          <div 
            className={styles.toast_complete}
            onClick={() => router.push('/tarot?info=past')}
          >
            기분좋은 하루, 오늘의 첫 카드를 뽑아보아요. (0/3) →
          </div>}
        {todayStatus === "past" && 
          <div className={styles.toast_past}
               onClick={() => router.push('/post')}
          >
            일기를 쓰면 카드를 획득할 수 있어요. 쓰러 가볼까요? (1/3) →
          </div>}
        {todayStatus === "now" && 
          <div 
            className={styles.toast_now}
            onClick={() => router.push('/tarot?info=future')}
          >
            마지막 카드를 뽑고, 오늘의 AI의 응원을 받아보세요. (2/3) →
          </div>}
      </div>
    </div>
  )

}

export default ToastModal;