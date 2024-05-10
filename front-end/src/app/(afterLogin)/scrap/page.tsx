"use client";

import styles from "./scrap.module.scss";
import ScrapCard from "@/app/_components/scrap/ScrapCard";
import { useEffect, useRef, useState } from "react";
import { ScrapItem } from "@/types/apis";
import useScrapStore from "@/store/stories";
import { useMutation } from "@tanstack/react-query";
import { deleteScrapData } from "@/libs/ScrapApis";
import ScrapPopup from "@/app/_components/scrap/ScrapPopup";

const Scrap: React.FC = () => {
  const target = useRef<HTMLDivElement>(null);
  const { scraps, loadScraps, hasMore } = useScrapStore();
  const deleteScrappedData = useMutation({
    mutationKey: ["deletedData"],
    mutationFn: (diaryId: number) => deleteScrapData(diaryId),
  });

  const [isOpen, setIsOpen] = useState<boolean>(false);
  const [cardInfo, setCardInfo] = useState<ScrapItem>();

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
      console.log(target);
      observer.observe(target.current);
    }

    return () => {
      if (target.current) observer.unobserve(target.current);
    };
  }, [scraps, hasMore]);

  useEffect(() => {
    return () => {
      scraps
        .filter((scrap) => !scrap.isScrapped)
        .forEach((scrap) => {
          deleteScrappedData.mutate(scrap.diaryId);
        });
    };
  }, [deleteScrapData]);

  return (
    scraps && (
      <section className={styles.scrap}>
        {scraps.map((card: ScrapItem, index: number) => (
          <ScrapCard
            key={`${card.diaryId}-${card.isScrapped}`}
            diaryId={card.diaryId}
            nickname={card.nickname}
            scrappedAt={card.scrappedAt}
            content={card.content}
            imgUrl={card.imgUrl}
            isScrapped={card.isScrapped}
            ref={index === 9 ? target : null}
            onClick={() => {
              setIsOpen(!isOpen);
              setCardInfo(card);
            }}
          />
        ))}
        {isOpen && cardInfo && (
          <ScrapPopup scrapInfo={cardInfo} onClose={setIsOpen} />
        )}
      </section>
    )
  );
};

export default Scrap;
