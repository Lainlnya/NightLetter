"use client";

import styles from "./tarot.module.scss";
import Spread from "@/app/_components/tarot/Spread";
import React, { useState } from "react";
import { useRouter } from "next/navigation";

interface SelectTarotProps {
  timePeriod?: "past" | "future";
}
interface CardInfo {
  no: number;
  name: string;
  right: string;
  reverse: string;
  description: string;
}

const SelectTarot: React.FC = ({ timePeriod }: SelectTarotProps) => {
  const [isTilted, setIsTilted] = useState<boolean>(true);
  const router = useRouter();

  const handleCardSelect = () => {
    router.push(`/card?isPast=${timePeriod}`);
  };

  /**
   * 셔플 버튼
   */
  const handleShuffle = () => {
    setIsTilted(false);
    setTimeout(() => setIsTilted(true), 10);
  };

  return (
    <main className={styles.main}>
      <h1 className={styles.h1}>
        <p>{timePeriod === "future" ? "미래" : "어제"}를 생각하며</p>
        <p>한 장을 뽑아보세요</p>
      </h1>
      <section className={styles.spreadSec}>
        <Spread isTilted={isTilted} handleSelectCard={handleCardSelect} />
      </section>
      <button className={styles.shuffleBtn} onClick={handleShuffle}>
        셔플
      </button>
    </main>
  );
};

export default SelectTarot;
