"use client";

import styles from "./card.module.scss";
import { getTarotCard } from "@/_apis/TarotApis";
import Loading from "@/app/loading";
import { useEffect, useState } from "react";
import {
  ReadonlyURLSearchParams,
  useRouter,
  useSearchParams,
} from "next/navigation";

interface CardInfo {
  no: number;
  name: string;
  right: string;
  reverse: string;
  description: string;
}

const ViewCard: React.FC = () => {
  const [card, setCard] = useState<CardInfo>();
  const [isBack, setIsBack] = useState<boolean>(true);

  const router = useRouter();
  const searchParams: ReadonlyURLSearchParams = useSearchParams();
  useEffect(() => {
    setCard(getTarotCard);
  }, []);
  if (!card) {
    return <Loading loadingMessage="로딩중입니다." />;
  }
  return (
    card && (
      <main className={styles.main}>
        {searchParams.get("info") === "present" ? (
          <div>오늘의 감정을 나타내는 카드는...</div>
        ) : (
          ""
        )}
        <div>{card.name}</div>
        <div className={`${styles.card} ${isBack ? styles.isRight : ""}`}>
          <img
            className={styles.front}
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
        {searchParams.get("info") === "past" ? (
          ""
        ) : (
          <button
            className={styles.comment}
            onClick={() => router.push("/comment")}
          >
            코멘트 보러가기
          </button>
        )}
      </main>
    )
  );
};

export default ViewCard;
