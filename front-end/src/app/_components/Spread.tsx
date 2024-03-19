import { useState } from "react";
import styles from "../_styles/Spread.module.scss";

interface SpreadProps {
  isTilted: boolean;
}

const Spread: React.FC<SpreadProps> = ({ isTilted }) => {
  return (
    <>
      <div
        className={`${styles.card} ${styles.card1} ${
          isTilted ? styles.tilt : ""
        }`}
      ></div>
      <div
        className={`${styles.card} ${styles.card2} ${
          isTilted ? styles.tilt : ""
        }`}
      ></div>
      <div
        className={`${styles.card} ${styles.card3} ${
          isTilted ? styles.tilt : ""
        }`}
      ></div>
      <div
        className={`${styles.card} ${styles.card4} ${
          isTilted ? styles.tilt : ""
        }`}
      ></div>
    </>
  );
};

export default Spread;
