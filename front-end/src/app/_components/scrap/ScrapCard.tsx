"use client";

import { ScrapItem } from "@/types/apis";
import styles from "../../(afterLogin)/scrap/scrap.module.scss";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faStar as farFaStar } from "@fortawesome/free-regular-svg-icons";
import { faStar as fasFaStar } from "@fortawesome/free-solid-svg-icons";
import { formattedDate } from "@/utils/dateFormat";
import { forwardRef } from "react";
import useScrapStore from "@/store/stories";

const ScrapCard = forwardRef<HTMLDivElement, ScrapItem>((props, ref) => {
  const { diaryId, nickname, content, scrappedAt, onClick } = props;

  const { toggleScrap, isScrappedDiary } = useScrapStore();

  const handleClick = (
    e: React.MouseEvent<HTMLButtonElement> | React.TouchEvent<HTMLButtonElement>
  ) => {
    e.stopPropagation();
    toggleScrap(diaryId);
  };

  return (
    <section ref={ref} className={styles.cardSec} onClick={onClick}>
      <h1>{nickname}</h1>
      <div className={styles.cardDate}>{formattedDate(scrappedAt)}</div>
      <div className={styles.cardContent}>{content}</div>
      <button className={styles.cardBtn} onClick={handleClick}>
        <FontAwesomeIcon
          icon={isScrappedDiary(diaryId) ? fasFaStar : farFaStar}
        />
      </button>
    </section>
  );
});

ScrapCard.displayName = "ScrapCard";

export default ScrapCard;
