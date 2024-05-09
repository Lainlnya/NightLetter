"use client";

import React, { useEffect } from "react";
import styles from "./stories.module.scss";
import { motion, useMotionValue } from "framer-motion";
import Image from "next/image";
import { useState } from "react";
import { useRouter } from "next/navigation";
import closeIcon from "../../../../public/Icons/xmark-solid.svg";
import tarotImg from "../../../../public/images/tarot-background.png";
import { DRAG_BUFFER } from "@/utils/animation";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faStar as farFaStar } from "@fortawesome/free-regular-svg-icons"; // 비어있음
import { faStar as fasFaStar } from "@fortawesome/free-solid-svg-icons"; // 가득참
import useScrapStore from "@/store/stories";
import { setScrapData } from "@/libs/ScrapApis";
import { useMutation } from "@tanstack/react-query";
import { ScrapItem } from "@/types/apis";
import useRecomStore from "@/store/recommendations";

export default function Diaries() {
  const router = useRouter();
  const cardWidthRem = 30.5;
  const [dragging, setDragging] = useState(false);
  const [contents, setContents] = useState([
    {
      content: "조회중입니다",
      imgUrl: `url(${tarotImg})`,
      nickname: "tarot",
      diaryId: -1,
    },
  ]);
  const [cardIndex, setCardIndex] = useState(0);
  const [nickname, setNickname] = useState(contents[0]?.nickname || "익명");
  const { scraps, isScrappedDiary, addScrap, toggleScrap } = useScrapStore();
  const { stories } = useRecomStore();

  const setScrappedData = useMutation({
    mutationKey: ["scrappedData"],
    mutationFn: (diaryId: number) => setScrapData(diaryId),
  });

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

  const handleScrap = (diary: ScrapItem) => {
    const { diaryId, nickname, content, imgUrl, scrappedAt } = diary;
    if (!isScrappedDiary(diary.diaryId)) {
      console.log("scrapped");
      addScrap({
        diaryId,
        nickname,
        content,
        imgUrl,
        scrappedAt,
        isScrapped: true,
      });
      setScrappedData.mutate(diaryId);
      console.log(scraps);
    } else {
      toggleScrap(diary.diaryId);
    }
  };

  useEffect(() => {
    console.log(stories);
    if (stories) {
      setContents(stories);
    }
  }, []);

  useEffect(() => {
    if (contents[cardIndex]) {
      const updatedNickname = contents[cardIndex]?.nickname || "익명";
      setNickname(updatedNickname);
    }
  }, [cardIndex, contents]);

  return (
    <section className={styles.root}>
      <Image
        className={styles.back}
        src={closeIcon}
        alt='뒤로가기'
        width={30}
        height={30}
        onClick={() => router.replace("/")}
      />
      <header className={styles.header}>
        <p>
          <span className={styles.nickname}>{nickname}</span>님의 <br /> 사연이
          도착했습니다.
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
            translateX: `-${cardIndex * cardWidthRem}rem`,
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
              const isSelected = idx === cardIndex;
              const storyStyle = {
                backgroundImage: `url(${isSelected ? diary.imgUrl : tarotImg})`,
              };
              return (
                <main
                  key={idx}
                  className={`${styles.story} ${
                    !isSelected ? styles.inactive : ""
                  }`}
                  style={storyStyle}
                >
                  {isSelected && (
                    <section className={styles.scrap}>
                      <FontAwesomeIcon
                        className={styles.star}
                        icon={diary.isScrapped ? fasFaStar : farFaStar}
                        onClick={() => handleScrap(diary)}
                      />
                      <div className={styles.story_contents}>
                        {diary.content}
                      </div>
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
