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
import Image from "next/image";
import { Messages } from "@/utils/msg";
import { useQuery } from "@tanstack/react-query";

interface CardInfo {
  cardNo: number;
  cardName: string;
  cardImgUrl: string;
  cardKeyWord: string;
  cardDesc: string;
}

const ViewCard: React.FC = () => {
  const [card, setCard] = useState<CardInfo>();
  const [isBack, setIsBack] = useState<boolean>(true);

  const router = useRouter();
  const searchParams: ReadonlyURLSearchParams = useSearchParams();

  const { isLoading, data: pastCard } = useQuery({
    queryKey: ["tarotCard"],
    queryFn: () => getTarotCard(searchParams.get("info") as string),
  });

  useEffect(() => {
    const presentCardInfo = sessionStorage.getItem("presentCardInfo");
    if (searchParams.get("info") === "present" && presentCardInfo !== null) {
      const { cardNo, cardName, cardImgUrl, cardKeyWord, cardDesc } =
        JSON.parse(presentCardInfo);
      setCard({ cardNo, cardName, cardImgUrl, cardKeyWord, cardDesc });
    }

    if (pastCard && searchParams.get("info") === "past") {
      setCard(pastCard);
    }
  }, [pastCard]);

  if (!card || isLoading) {
    return <Loading loadingMessage={Messages.LOADING_CARD_INFO} />;
  }

  return (
    card && (
      <main className={styles.main}>
        {searchParams.get("info") === "present" && (
          <div className={styles.today}>오늘의 감정을 나타내는 카드는...</div>
        )}
        <div>{card.cardName}</div>
        <div className={`${styles.card} ${isBack ? styles.isRight : ""}`}>
          <Image
            className={styles.front}
            src={card.cardImgUrl}
            alt="tarot image"
            width={230}
            height={400}
          />
          <div>
            <Image
              className={styles.back}
              onClick={() => setIsBack(!isBack)}
              src={card.cardImgUrl}
              alt="tarot image"
              width={230}
              height={400}
            />
            <div
              className={`${isBack ? styles.nonView : styles.view}`}
              onClick={() => setIsBack(!isBack)}
            >
              {card.cardDesc}
            </div>
          </div>
        </div>
        <div className={styles.meaning}>{card.cardKeyWord}</div>
        {searchParams.get("info") === "present" && (
          <button
            className={styles.comment}
            onClick={() => router.push("/tarot?info=future")}
          >
            미래카드 뽑기
          </button>
        )}
        {searchParams.get("info") === "future" && (
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
