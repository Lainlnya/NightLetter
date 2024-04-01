"use client";

import styles from "./tarot.module.scss";
import Spread from "@/app/_components/tarot/Spread";
import React, { useState } from "react";
import {
  ReadonlyURLSearchParams,
  useRouter,
  useSearchParams,
} from "next/navigation";

const SelectTarot: React.FC = () => {
  const [isTilted, setIsTilted] = useState<boolean>(true);
  const router = useRouter();
  const searchParams: ReadonlyURLSearchParams = useSearchParams();

  const handleCardSelect = () => {
    router.push(`/card?info=${searchParams.get("info")}`);
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
        <p>
          {searchParams.get("info") === "future" ? "미래" : "어제"}를 생각하며
        </p>
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
