"use client";

import styles from "@/app/_styles/Card.module.scss";
import { getTarotCard } from "@/app/_apis/TarotApis";
import Loading from "@/app/loading";
import { useEffect, useState } from "react";

interface CardInfo {
  no: number;
  name: string;
  right: string;
  reverse: string;
  description: string;
}

const ViewCard: React.FC = () => {
  const [card, setCard] = useState<CardInfo>();
  const [isBack, setIsBack] = useState<boolean>(false);
  useEffect(() => {
    setCard(getTarotCard);
  }, []);
  if (!card) {
    return <Loading loadingMessage="로딩중입니다." />;
  }
  return (
    card && (
      <main className={styles.main}>
        <div>{card.name}</div>
        <div className={`${styles.card} ${isBack ? styles.isRight : ""}`}>
          <img
            className={styles.front}
            onClick={() => setIsBack(!isBack)}
            src={`/deleted_card/${card.no}.png`}
            alt="tarot image"
          />
          <div>
            <img
              className={styles.back}
              onClick={() => setIsBack(!isBack)}
              src={`/deleted_card/${card.no}.png`}
              alt="tarot image"
            />
            <div
              className={`${isBack ? styles.nonView : styles.view}`}
              onClick={() => setIsBack(!isBack)}
            >
              {card.description}
            </div>
          </div>
        </div>
        <div className={styles.meaning}>{card.right}</div>
      </main>
    )
  );
};

export default ViewCard;
