'use client';
import Toast from '@/app/_components/post/Toast';
import styles from './post.module.scss';
import { useEffect, useRef, useState } from 'react';
import { useMutation } from '@tanstack/react-query';
import { setData } from '@/libs/DiaryApis/setData';
import { Messages } from '@/utils/msg';
import { useRouter } from 'next/navigation';
import { TODAY, getNextDate } from '@/utils/dateFormat';
import Loading from '@/app/loading';

const Post: React.FC = () => {
  const [diaryText, setDiaryText] = useState('');
  const [toast, setToast] = useState<boolean>(false);
  const [checked, setChecked] = useState<boolean>(false);
  const [debouncedValue, setDebouncedValue] = useState<string>(diaryText);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const router = useRouter();
  const diaryRef = useRef(null);

  /** 임시저장 함수 */
  const saveTemp = () => {
    setToast(true);
    if (debouncedValue.trim().length !== 0) {
      localStorage.setItem('diaryPost', JSON.stringify([debouncedValue, getNextDate()]));
    }
  };

  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedValue(diaryText);
    }, 500);

    return () => {
      clearTimeout(handler);
    };
  }, [diaryText]);

  /** 체크박스 값 감지 */
  const handleCheckboxChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setChecked(event.target.checked);
  };

  useEffect(() => {
    // 다이어리 임시저장 정보 가져오기
    const storedDiary = localStorage.getItem('diaryPost');
    if (storedDiary) {
      const diaryData: [string, Date] = JSON.parse(storedDiary);
      // 다이어리 저장할 때 내일 오전 4시를 기준으로 저장 => diaryData[1].getTime()
      // new Date().getTime()
      if (new Date().getTime() > new Date(diaryData[1]).getTime()) {
        localStorage.removeItem('diaryPost');
      } else {
        setDiaryText(diaryData[0]);
      }
    }
  }, []);

  useEffect(() => {
    return () => {
      setIsLoading(false);
    };
  }, []);

  const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setDiaryText(e.target.value);
  };

  /** 다이어리 저장 쿼리 */
  const setDiary = useMutation({
    mutationKey: ['post'],
    mutationFn: () => setData({ content: diaryText, type: checked ? 'PUBLIC' : 'PRIVATE' }),
    onMutate: () => {
      setIsLoading(true);
    },
    onSuccess: (value) => {
      sessionStorage.setItem('presentCardInfo', JSON.stringify(value));

      localStorage.removeItem('diaryPost');
      router.push('/card?info=present');
    },
    onError: () => {
      console.log('An error occurred');
    },
  });

  /** 저장 버튼 눌렀을 때 작동하는 함수 */
  const handleSave = () => {
    if (diaryText.trim().length === 0) {
      setToast(true);
    } else setDiary.mutate();
  };

  return (
    <section>
      {isLoading ? (
        <Loading loadingMessage={Messages.LOADING_CARD_INFO} />
      ) : (
        <section className={styles.post}>
          <h1>오늘의 일기를 작성해주세요</h1>
          <div className={styles.hr_sect}>{TODAY}</div>
          <textarea
            onChange={handleChange}
            ref={diaryRef}
            value={diaryText}
            placeholder="일기를 작성해주세요"
            maxLength={600}
          ></textarea>
          <div className={styles.temperary}>
            {toast &&
              (diaryText.trim().length === 0 ? (
                <Toast setToast={setToast} text={Messages.DIARY_POST_NO_CONTENT} />
              ) : (
                <Toast setToast={setToast} text={Messages.DIARY_TEMPORARY_SAVE_SUCCESS} />
              ))}
            <button onClick={saveTemp}>임시저장</button>
            <div>{diaryText.length}/600자</div>
          </div>
          <hr />
          <div className={styles.checkbox}>
            <input type="checkbox" name="public" id="public" checked={checked} onChange={handleCheckboxChange} />
            <label htmlFor="public">사연으로 공개할래요</label>
          </div>
          <button className={styles.save} onClick={handleSave}>
            저장하기
          </button>
        </section>
      )}
    </section>
  );
};

export default Post;
