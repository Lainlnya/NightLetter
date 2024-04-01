"use client";
import Toast from "@/app/_components/post/Toast";
import styles from "./post.module.scss";
import { useEffect, useState } from "react";
import { useMutation } from "@tanstack/react-query";
import { setData } from "@/_apis/DiaryApis";
import { Messages } from "@/utils/msg";
import { useRouter } from "next/navigation";
import { TODAY } from "@/utils/dateFormat";

/** 일기 작성을 위한 debounce 함수*/
const debounce = <T extends (...args: any[]) => any>(fn: T, delay: number) => {
  let timeout: ReturnType<typeof setTimeout>;

  return (...args: Parameters<T>): ReturnType<T> => {
    let result: any;
    if (timeout) clearTimeout(timeout);
    timeout = setTimeout(() => {
      result = fn(...args);
    }, delay);
    return result;
  };
};

const Post: React.FC = () => {
  // 내일의 날짜
  const tomorrowDate = new Date();
  tomorrowDate.setDate(new Date().getDate() + 1);
  tomorrowDate.setHours(4, 0, 0, 0); // 내일 오전 4시

  const [diaryText, setDiaryText] = useState("");
  const [toast, setToast] = useState<boolean>(false);
  const [checked, setChecked] = useState<boolean>(false);
  const [debounceState, setDebounceState] = useState<string>("");
  const router = useRouter();

  /** 임시저장 함수 */
  const saveTemp = () => {
    setToast(true);
    if (debounceState.trim().length !== 0) {
      localStorage.setItem(
        "diaryPost",
        JSON.stringify([debounceState, tomorrowDate])
      );
    }
  };

  const onChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setDebounceState(e.target.value);
  };

  /** 변하는 값 감지 및 글자수 감지 함수 */
  const debouncedOnChange = debounce<typeof onChange>(onChange, 300);

  /** 체크박스 값 감지 */
  const handleCheckboxChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setChecked(event.target.checked);
  };

  useEffect(() => {
    // 다이어리 임시저장 정보 가져오기
    const storedDiary = localStorage.getItem("diaryPost");
    if (storedDiary) {
      const diaryData: [string, Date] = JSON.parse(storedDiary);
      // 다이어리 저장할 때 내일 오전 4시를 기준으로 저장 => diaryData[1].getTime()
      // new Date().getTime()
      if (new Date().getTime() > new Date(diaryData[1]).getTime()) {
        localStorage.removeItem("diaryPost");
      } else {
        setDebounceState(diaryData[0]);
      }
    }
  }, []);

  /** 다이어리 저장 쿼리 */
  const setDiary = useMutation({
    mutationKey: ["post"],
    mutationFn: () =>
      setData({ content: debounceState, type: checked ? "PUBLIC" : "PRIVATE" })
        .then((value) => {
          sessionStorage.setItem("presentCardInfo", JSON.stringify(value));
          localStorage.removeItem("diaryPost");
          router.push("/card?info=present");
        })
        .catch((e) => console.log(e)),
  });

  /** 저장 버튼 눌렀을 때 작동하는 함수 */
  const handleSave = () => {
    if (debounceState.trim().length === 0) {
      setToast(true);
    } else setDiary.mutate();
  };

  return (
    <main className={styles.post}>
      <h1>오늘의 일기를 작성해주세요</h1>
      <div className={styles.hr_sect}>{TODAY}</div>
      <textarea
        onChange={debouncedOnChange}
        placeholder="일기를 작성해주세요"
        maxLength={600}
      ></textarea>
      <div className={styles.temperary}>
        {toast &&
          (debounceState.trim().length === 0 ? (
            <Toast setToast={setToast} text={Messages.DIARY_POST_NO_CONTENT} />
          ) : (
            <Toast
              setToast={setToast}
              text={Messages.DIARY_TEMPORARY_SAVE_SUCCESS}
            />
          ))}
        <button onClick={saveTemp}>임시저장</button>
        <div>{debounceState.length}/600자</div>
      </div>
      <hr />
      <div className={styles.checkbox}>
        <input
          type="checkbox"
          name="public"
          id="public"
          checked={checked}
          onChange={handleCheckboxChange}
        />
        <label htmlFor="public">사연으로 공개할래요</label>
      </div>
      <button className={styles.save} onClick={handleSave}>
        저장하기
      </button>
    </main>
  );
};

export default Post;
