import Image from "next/image";
import styles from "./page.module.scss";
import alarm from "../../public/Icons/alarm_icon.svg";
import calender from "../../public/Icons/calender_icon.svg"
import tarot_background from "../../public/images/tarot-background.png";

// TODO: 
// 1.서버 데이터 받아서 날짜 동적으로 수정
// 2. 캐러셀 제대로 만들기
// 3. 아이콘 클릭시 캘린더, 알림페이지로 이동하기
// 4. 조건에 따라 알림 띄우기


export default function Home() {
  return <main className={styles.main}>
    <header className={styles.header}>
      <div className={styles.header_icons}>
        <Image src={alarm} alt="alarm" width={24} height={24} className={styles.header_icon} />
        <Image src={calender} alt="calender" width={24} height={24} className={styles.header_icon} />
      </div>
      <div className={styles.header_title}>
        <h1>2024년 3월 11일</h1>
      </div>
    </header>

    <section className={styles.section}>
      <div className={styles.guide}>
        카드를 슬라이드하여 날짜를 바꿀 수 있어요.
      </div>
      <div className={styles.carousel}>
        <div className={styles.card_container}>
          <div className={styles.card_wrapper}>
            <Image src={tarot_background} className={`${styles.card} ${styles.past}`} alt="past_card" width={120} height={205} />
            <Image src={tarot_background} className={`${styles.card} ${styles.current}`} alt="current_card" width={120} height={205} />
            <Image src={tarot_background} className={`${styles.card} ${styles.future}`} alt="future_card" width={120} height={205} />
          </div>
        </div>
      </div>

      <div></div>
    </section>
    <div className={styles.capsule_notification}>
      AI가 분석한 오늘의 코멘트가 도착했어요.
    </div>
  </main>;
}
