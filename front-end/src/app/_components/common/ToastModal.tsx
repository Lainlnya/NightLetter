'use client'

import styles from '@/app/_components/common/toastModal.module.scss';
import { useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';
import checkTodayStatus from '@/libs/checkTodayStatus';

const ToastModal = () => {
  const router = useRouter();
  const [todayStatus, setTodayStatus] = useState("");

  useEffect(() => {
    async function fetchData() {
      try {
        const data = await checkTodayStatus();
        console.log(data);
        const { pastCard, nowCard, futureCard } = await checkTodayStatus();
        if (pastCard && nowCard && futureCard) setTodayStatus("complete");
        if (!pastCard && !nowCard && !futureCard) setTodayStatus("not checked");
        if (pastCard && nowCard && !futureCard) setTodayStatus("now");
        if (pastCard && !nowCard && !futureCard) setTodayStatus("past");
      } catch (error) {
        console.error("오늘의 카드를 조회하는데 실패했어요.");
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
            오늘의 첫 카드를 뽑아주세요. (0/3) →
          </div>}
        {todayStatus === "past" && 
          <div className={styles.toast_past}
               onClick={() => router.push('/post')}
          >
            이제 일기를 쓸 차례에요. 쓰러 가보실까요? (1/3) →
          </div>}
        {todayStatus === "now" && 
          <div 
            className={styles.toast_now}
            onClick={() => router.push('/tarot?info=future')}
          >
            마지막으로 카드를 뽑고, AI의 조언을 받아보세요. (2/3) →
          </div>}
      </div>
    </div>
  )

}

export default ToastModal;