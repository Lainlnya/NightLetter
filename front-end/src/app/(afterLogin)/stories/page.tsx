'use client';

import React, { useEffect } from 'react';
import styles from './stories.module.scss';
import { motion, useMotionValue } from 'framer-motion';
import Image from 'next/image';
import { useState } from 'react';
import { useRouter } from 'next/navigation';
import closeIcon from '../../../../public/Icons/xmark-solid.svg';
import tarotImg from '../../../../public/images/tarot-background.png';
import { DRAG_BUFFER } from '@/utils/animation';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faStar as farFaStar } from '@fortawesome/free-regular-svg-icons'; // 비어있음
import { faAngleLeft, faAngleRight, faStar as fasFaStar } from '@fortawesome/free-solid-svg-icons'; // 가득참
import { deleteScrapData } from '@/libs/ScrapApi/deleteScrapData';
import { setScrapData } from '@/libs/ScrapApi/setScrapData';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { ScrapItem } from '@/types/apis';
import { getRecommendDiaries } from '@/libs/DiaryApis/getRecommendDiaries';

export default function Diaries() {
  const router = useRouter();
  const cardWidthRem = 30.5;
  const [dragging, setDragging] = useState(false);
  const queryClient = useQueryClient();
  const [contents, setContents] = useState([
    {
      diaryId: -1,
      nickname: 'tarot',
      content: '조회중입니다',
      imgUrl: `url(${tarotImg})`,
      isScrapped: false,
    },
  ]);
  const [cardIndex, setCardIndex] = useState(0);
  const [nickname, setNickname] = useState(contents[0]?.nickname || '익명');

  // 스크랩 관련
  const { data: recomDataFromApi } = useQuery({
    queryKey: ['recommendations'],
    queryFn: () => getRecommendDiaries(),
  });

  // 스크랩하기
  const addScrappedData = useMutation({
    mutationKey: ['AddedData'],
    mutationFn: (diaryId: number) => setScrapData(diaryId),
    onMutate: async (diaryId) => {
      await queryClient.cancelQueries({ queryKey: ['recommendations'] });

      const previousData = queryClient.getQueryData(['recommendations']);
      console.log('previousData:', previousData);

      queryClient.setQueryData(['recommendations'], (old: ScrapItem[]) => {
        return old.map((item) => (item.diaryId === diaryId ? { ...item, isScrapped: !item.isScrapped } : item));
      });

      setContents((prevContents) =>
        prevContents.map((item) => (item.diaryId === diaryId ? { ...item, isScrapped: !item.isScrapped } : item))
      );

      return () => queryClient.setQueryData(['recommendations'], previousData);
    },
    onError: (error, diaryId, rollback) => {
      if (rollback) rollback();
      else console.log(error);
    },
    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: ['recommendations'] });
    },
  });

  // 스크랩 취소
  const deleteScrappedData = useMutation({
    mutationKey: ['DeletedData'],
    mutationFn: (diaryId: number) => deleteScrapData(diaryId),
    onMutate: async (diaryId) => {
      await queryClient.cancelQueries({ queryKey: ['recommendations'] });

      const previousData = queryClient.getQueryData(['recommendations']);
      console.log('rm:', previousData);

      queryClient.setQueryData(['recommendations'], (old: ScrapItem[]) => {
        return old.map((item) => (item.diaryId === diaryId ? { ...item, isScrapped: !item.isScrapped } : item));
      });

      setContents((prevContents) =>
        prevContents.map((item) => (item.diaryId === diaryId ? { ...item, isScrapped: !item.isScrapped } : item))
      );

      return () => queryClient.setQueryData(['recommendations'], previousData);
    },
    onError: (error, diaryId, rollback) => {
      if (rollback) rollback();
      else console.log(error);
    },
    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: ['recommendations'] });
    },
  });

  const handleScrap = (diary: ScrapItem) => {
    if (diary.isScrapped) {
      deleteScrappedData.mutate(diary.diaryId);
    } else {
      addScrappedData.mutate(diary.diaryId);
    }
  };

  // 드래그 관련
  const dragX = useMotionValue(0);

  const onDragStart = () => {
    setDragging(true);
  };

  const onDragEnd = () => {
    setDragging(false);

    const x = dragX.get(); // 현재 드래그 위치

    if (x <= -DRAG_BUFFER && cardIndex < contents.length - 1) {
      setCardIndex((prev) => prev + 1);
    } else if (x >= DRAG_BUFFER && cardIndex > 0) {
      setCardIndex((prev) => prev - 1);
    }
  };

  const handlePrev = () => {
    setCardIndex((prevIndex) => Math.max(prevIndex - 1, 0));
  };

  const handleNext = () => {
    setCardIndex((prevIndex) => Math.min(prevIndex + 1, contents.length - 1));
  };

  useEffect(() => {
    if (recomDataFromApi) {
      setContents(recomDataFromApi);
    }
  }, [recomDataFromApi]);

  useEffect(() => {
    if (contents[cardIndex]) {
      const updatedNickname = contents[cardIndex]?.nickname || '익명';
      setNickname(updatedNickname);
    }
  }, [cardIndex, contents]);

  return (
    <section className={styles.root}>
      <Image
        className={styles.back}
        src={closeIcon}
        alt="뒤로가기"
        width={30}
        height={30}
        onClick={() => router.replace('/')}
      />
      <header className={styles.header}>
        <p>
          <span className={styles.nickname}>{nickname}</span>님의 <br /> 사연이 도착했습니다.
        </p>
      </header>
      <div className={styles.carousel_container}>
        <motion.div
          drag="x"
          dragConstraints={{
            left: 0,
            right: 0,
          }}
          animate={{
            translateX: `-${cardIndex * cardWidthRem}rem`,
          }}
          style={{
            x: dragX,
          }}
          onDragStart={onDragStart}
          onDragEnd={onDragEnd}
          className={styles.carousel}
        >
          <Image className={styles.prev} src={tarotImg} width={290} height={480} alt="타로" />
          {contents &&
            contents.map((diary, idx) => {
              const isSelected = idx === cardIndex;
              const storyStyle = {
                backgroundImage: `url(${isSelected ? diary.imgUrl : tarotImg})`,
              };
              return (
                <main key={idx} className={`${styles.story} ${!isSelected ? styles.inactive : ''}`} style={storyStyle}>
                  {isSelected && (
                    <section className={styles.scrap}>
                      {idx !== 0 && <FontAwesomeIcon className={styles.left} icon={faAngleLeft} onClick={handlePrev} />}
                      <FontAwesomeIcon
                        className={styles.star}
                        icon={diary.isScrapped ? fasFaStar : farFaStar}
                        onClick={() => handleScrap(diary)}
                      />
                      <div className={styles.story_contents}>{diary.content}</div>
                      {idx !== contents.length - 1 && (
                        <FontAwesomeIcon className={styles.right} icon={faAngleRight} onClick={handleNext} />
                      )}
                    </section>
                  )}
                </main>
              );
            })}
        </motion.div>
      </div>
      <footer className={styles.footer}>
        <p>슬라이드하여 다른 일기를 조회할 수 있어요.</p>
      </footer>
    </section>
  );
}
