"use client";

import styles from "./scrap.module.scss";
import ScrapCard from "@/app/_components/common/ScrapCard";
import { useEffect, useRef } from "react";
import { ScrapItem } from "@/types/apis";
import useScrapStore from "@/store/stories";

const Scrap: React.FC = () => {
  const target = useRef<HTMLDivElement>(null);
  const { scraps, loadScraps, hasMore } = useScrapStore();

  useEffect(() => {
    loadScraps();
  }, []);

  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && hasMore) {
          loadScraps();
        }
      },
      {
        threshold: 1,
      }
    );
    if (target.current) {
      observer.observe(target.current);
    }
    return () => {
      if (target.current) observer.unobserve(target.current);
    };
  }, [scraps.length, hasMore]);

  return (
    scraps && (
      <section className={styles.scrap}>
        {scraps.map((card: ScrapItem, index: number) => (
          <ScrapCard
            key={card.diaryId}
            diaryId={card.diaryId}
            nickname={card.nickname}
            scrappedAt={card.scrappedAt}
            content={card.content}
            imgUrl={card.imgUrl}
            ref={index === 9 ? target : null}
          />
        ))}
      </section>
    )
  );
};

export default Scrap;
