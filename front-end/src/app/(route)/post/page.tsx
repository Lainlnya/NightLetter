"use client";
import styles from "@/app/_styles/Post.module.scss";
import { useState } from "react";

const Post: React.FC = () => {
  const today = new Date();
  const [year, month, day] = [
    today.getFullYear(),
    today.getMonth() + 1,
    today.getDate(),
  ];

  const formattedDate = `${year.toString().substr(2)}.${month
    .toString()
    .padStart(2, "0")}.${day.toString().padStart(2, "0")}`;

  const [textCount, setTextCount] = useState<number>(0);
  const onInputHandler = (_e: any) => {
    setTextCount(_e.target.value.length);
  };

  return (
    <main className={styles.post}>
      <h1>오늘의 일기를 작성해주세요</h1>
      <div className={styles.hr_sect}>{formattedDate}</div>
      <textarea
        onChange={onInputHandler}
        placeholder="일기를 작성해주세요"
        maxLength="600"
      ></textarea>
      <div className={styles.temperary}>
        <button>임시저장</button>
        <div>{textCount}/600자</div>
      </div>
      <hr />
      <div className={styles.checkbox}>
        <input type="checkbox" name="public" />
        <label htmlFor="public">사연으로 공개할래요</label>
      </div>
      <button className={styles.save}>저장하기</button>
    </main>
  );
};

export default Post;
