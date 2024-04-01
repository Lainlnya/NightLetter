'use client';

import React, { useEffect } from 'react';
import styles from './stories.module.scss';
import { motion, useMotionValue } from 'framer-motion';
import { useState } from 'react';
import { useRouter } from 'next/navigation';
import tarot_background from '../../../../public/images/tarot-background.png';

const DRAG_BUFFER = 100;

export default function Diaries() {
  const router = useRouter();
  const [dragging, setDragging] = useState(false);
  const [contents, setContents] = useState([
    { content: '조회중입니다', imgUrl: `url(${tarot_background})`, nickname: 'tarot' },
  ]);
  const [cardIndex, setCardIndex] = useState(0);
  const [nickname, setNickname] = useState(contents[0]?.nickname || '익명');

  const dragX = useMotionValue(0);

  const onDragStart = () => {
    setDragging(true);
  };

  const onDragEnd = () => {
    setDragging(false);

    const x = dragX.get();

    if (x <= -DRAG_BUFFER && cardIndex < contents.length - 1) {
      setCardIndex((prev) => prev + 1);
    } else if (x >= DRAG_BUFFER && cardIndex > 0) {
      setCardIndex((prev) => prev - 1);
    }
  };

  useEffect(() => {
    const recommendList = sessionStorage.getItem('presentCardInfo');
    if (recommendList) {
      const { recommendDiaries } = JSON.parse(recommendList);
      setContents(recommendDiaries);
    }
  }, []);

  useEffect(() => {
    if (contents[cardIndex]) {
      const updatedNickname = contents[cardIndex]?.nickname || '익명';
      setNickname(updatedNickname);
    }
    console.log('index' + cardIndex);
  }, [cardIndex, contents]);

  return (
    <div className={styles.root}>
      <header className={styles.header}>
        <p>
          <span className={styles.nickname}>{nickname}</span>님의 <br /> 사연이 도착했습니다.
        </p>
      </header>
      <div className={styles.carousel_container}>
        <motion.div
          drag='x'
          dragConstraints={{
            left: 0,
            right: 0,
          }}
          animate={{
            translateX: `-${cardIndex * 30.5}rem`,
          }}
          style={{
            x: dragX,
          }}
          onDragStart={onDragStart}
          onDragEnd={onDragEnd}
          className={styles.carousel}
        >
          {contents &&
            contents.map((diary, idx) => {
              // 현재 선택된 카드만 내용을 보여주기 위한 조건
              const isSelected = idx === cardIndex;
              return (
                <main
                  key={idx}
                  className={`${styles.story} ${!isSelected ? styles.inactive : ''}`}
                  style={{
                    backgroundImage: isSelected ? `url(${diary.imgUrl})` : `url(${tarot_background})`,
                    backgroundSize: '100% 100%',
                    width: `28.9rem`,
                    height: `47.9rem`,
                    borderRadius: `2rem`,
                  }}
                >
                  {isSelected && <div className={styles.story_contents}>{diary.content}</div>}
                </main>
              );
            })}
        </motion.div>
      </div>

      <footer className={styles.footer}>
        <p>슬라이드하여 다른 일기를 조회할 수 있어요.</p>
      </footer>
    </div>
  );
}
