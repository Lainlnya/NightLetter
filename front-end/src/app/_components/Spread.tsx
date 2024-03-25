"use client";

import styles from "../_styles/Spread.module.scss";
interface SpreadProps {
  isTilted: boolean;
  handleSelectCard: () => void;
}

const Spread: React.FC<SpreadProps> = ({ isTilted, handleSelectCard }) => {
  const handleClick = (_e: unknown) => {
    handleSelectCard();
  };

  return (
    <>
      <div
        className={`${styles.card} ${styles.card1} ${
          isTilted ? styles.tilt : ""
        }`}
        onClick={handleClick}
      ></div>
      <div
        className={`${styles.card} ${styles.card2} ${
          isTilted ? styles.tilt : ""
        }`}
        onClick={handleClick}
      ></div>
      <div
        className={`${styles.card} ${styles.card3} ${
          isTilted ? styles.tilt : ""
        }`}
        onClick={handleClick}
      ></div>
      <div
        className={`${styles.card} ${styles.card4} ${
          isTilted ? styles.tilt : ""
        }`}
        onClick={handleClick}
      ></div>
    </>
  );
};

export default Spread;
