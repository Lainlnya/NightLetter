"use client";
import Toast from "@/app/_components/post/Toast";
import styles from "./post.module.scss";
import { useEffect, useState } from "react";

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

  // 내일의 날짜
  const tomorrowDate = new Date();
  tomorrowDate.setDate(new Date().getDate() + 1);
  tomorrowDate.setHours(4, 0, 0, 0); // 내일 오전 4시

  const [textCount, setTextCount] = useState<number>(0);
  const [tempDiary, setTempDiary] = useState<string>("");
  const [toast, setToast] = useState<boolean>(false);

  // toast 추가 (입력해주세요)
  /** 임시저장 함수 */
  const saveTemp = () => {
    setToast(true);
    if (tempDiary.trim().length !== 0) {
      localStorage.setItem(
        "DiaryPost",
        JSON.stringify([tempDiary, tomorrowDate])
      );
      setTempDiary(tempDiary);
    }
  };

  /** 변하는 값 감지 및 글자수 감지 함수 */
  const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setTextCount(e.target.value.length);
    setTempDiary(e.target.value);
  };

  useEffect(() => {
    // 다이어리 임시저장 정보 가져오기
    const storedDiary = localStorage.getItem("DiaryPost");
    if (storedDiary) {
      const diaryData: [string, Date] = JSON.parse(storedDiary);
      // 다이어리 저장할 때 내일 오전 4시를 기준으로 저장 => diaryData[1].getTime()
      // new Date().getTime()
      if (new Date().getTime() > new Date(diaryData[1]).getTime()) {
        localStorage.removeItem("DiaryPost");
        setTempDiary("");
      } else {
        setTempDiary(diaryData[0]);
        setTextCount(diaryData[0].length);
      }
    }
  }, []);

  return (
    <main className={styles.post}>
      <h1>오늘의 일기를 작성해주세요</h1>
      <div className={styles.hr_sect}>{formattedDate}</div>
      <textarea
        value={tempDiary}
        onChange={handleChange}
        placeholder="일기를 작성해주세요"
        maxLength="600"
      ></textarea>
      <div className={styles.temperary}>
        {toast &&
          (tempDiary.trim().length === 0 ? (
            <Toast setToast={setToast} text="입력해주세요." />
          ) : (
            <Toast setToast={setToast} text="성공적으로 임시저장되었어요." />
          ))}
        <button onClick={saveTemp}>임시저장</button>
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
