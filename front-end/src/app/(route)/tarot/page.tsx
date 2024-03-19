"use client";

import styles from "@/app/_styles/Tarot.module.scss";
import Spread from "@/app/_components/Spread";
import React, { useState } from "react";

const SelectTarot: React.FC = () => {
  const [isTilted, setIsTilted] = useState<boolean>(true);

  const handleShuffle = () => {
    setIsTilted(false);
    setTimeout(() => setIsTilted(true), 10);
  };

  return (
    <main className={styles.main}>
      <h1 className={styles.h1}>
        <p>어제를 생각하며</p>
        <p>한 장을 뽑아보세요</p>
      </h1>
      <section className={styles.spreadSec}>
        <Spread isTilted={isTilted} />
      </section>
      <button className={styles.shuffleBtn} onClick={handleShuffle}>
        셔플
      </button>
    </main>
  );
};

export default SelectTarot;
