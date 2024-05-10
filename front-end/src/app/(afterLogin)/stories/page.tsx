"use client";

import React, { useEffect, useRef } from "react";
import styles from "./stories.module.scss";
import {
  motion,
  useAnimation,
  useMotionValue,
  useMotionValueEvent,
  useScroll,
} from "framer-motion";
import Image from "next/image";
import { useState } from "react";
import { useRouter } from "next/navigation";
import closeIcon from "../../../../public/Icons/xmark-solid.svg";
import tarotImg from "../../../../public/images/tarot-background.png";
import { DRAG_BUFFER } from "@/utils/animation";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faStar as farFaStar } from "@fortawesome/free-regular-svg-icons"; // 비어있음
import { faStar as fasFaStar } from "@fortawesome/free-solid-svg-icons"; // 가득참
import { deleteScrapData, setScrapData } from "@/libs/ScrapApis";
import { useMutation } from "@tanstack/react-query";
import { ScrapItem } from "@/types/apis";
import useRecomStore from "@/store/recommendations";
import { useStore } from "zustand";

export default function Diaries() {
  const router = useRouter();
  const cardWidthRem = 30.5;
  const [dragging, setDragging] = useState(false);
  const [contents, setContents] = useState([
    {
      diaryId: -1,
      nickname: "tarot",
      content: "조회중입니다",
      imgUrl: `url(${tarotImg})`,
      isScrapped: false,
    },
  ]);
  const [cardIndex, setCardIndex] = useState(0);
  const [nickname, setNickname] = useState(contents[0]?.nickname || "익명");

  const { toggleStories, isScrappedStories, setStories } = useRecomStore();
  const stories = useStore(useRecomStore, (state) => state.stories);

  const addScrappedData = useMutation({
    mutationKey: ["AddedData"],
    mutationFn: (diaryId: number) => setScrapData(diaryId),
  });

  const deleteScrappedData = useMutation({
    mutationKey: ["DeletedData"],
    mutationFn: (diaryId: number) => deleteScrapData(diaryId),
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
    if (isScrappedStories(diary.diaryId)) {
      deleteScrappedData.mutate(diary.diaryId);
    } else {
      addScrappedData.mutate(diary.diaryId);
    }
    toggleStories(diary.diaryId);
  };

  useEffect(() => {
    const storedData = localStorage.getItem("RecommendationStories");
    if (stories) {
      setContents(stories);
    } else if (storedData) {
      setStories(JSON.parse(storedData).state);
      setContents(stories);
    }
  }, [contents]);

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
                        icon={
                          isScrappedStories(diary.diaryId)
                            ? fasFaStar
                            : farFaStar
                        }
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
