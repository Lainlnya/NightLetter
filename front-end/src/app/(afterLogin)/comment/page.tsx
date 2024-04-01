import styles from "./comment.module.scss";
import Image from "next/image";

const Comment: React.FC = () => {
  const today = new Date();
  const [year, month, day] = [
    today.getFullYear(),
    today.getMonth() + 1,
    today.getDate(),
  ];
  const formattedDate = `${year}년 ${month}월 ${day}일`;
  return (
    <>
      <main className={styles.commentMain}>
        <h1>
          <div>{formattedDate}</div>
          <div>하루의 코멘트</div>
        </h1>
        <section className={styles.cardSec}>
          <section>
            <div>과거</div>
            <div>현재</div>
            <div>미래</div>
          </section>
          <section>
            <Image
              className={styles.past}
              src="/deleted_card/1.png"
              width={110}
              height={190}
              alt="과거 카드"
            />
            <Image
              className={styles.present}
              alt="현재 카드"
              src="/deleted_card/10.png"
              width={110}
              height={190}
            />
            <Image
              width={110}
              height={190}
              alt="미래 카드"
              className={styles.future}
              src="/deleted_card/21.png"
            />
          </section>
        </section>
        <section className={styles.commentSec}>
          이 세 카드의 조합은 과거의 어려움과 도전이 결국 더 큰 성장과
          풍요로움으로 이어질 것임을 나타냅니다.
        </section>
      </main>
    </>
  );
};

export default Comment;
