"use client";

import { ScrapItem } from "@/types/apis";
import styles from "../../(afterLogin)/scrap/scrap.module.scss";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faStar as farFaStar } from "@fortawesome/free-regular-svg-icons";
import { faStar as fasFaStar } from "@fortawesome/free-solid-svg-icons";
import { formattedDate } from "@/utils/dateFormat";
import { forwardRef } from "react";
import useScrapStore from "@/store/stories";
import { setScrapData, deleteScrapData } from "@/libs/ScrapApis";
import { useMutation } from "@tanstack/react-query";

const ScrapCard = forwardRef<HTMLDivElement, ScarpCardResponseBody>(
  (props: ScrapItem, ref) => {
    const {
      diaryId,
      nickname,
      content,
      imgUrl,
      scrappedAt,
      isScrapped,
    }: ScrapItem = props;

    const { addScrap, toggleScrap, isScrappedDiary } = useScrapStore();
    const setScrappedData = useMutation({
      mutationKey: ["scrappedData", diaryId],
      mutationFn: (diaryId: number) => setScrapData(diaryId),
    });

    const deleteScrappedData = useMutation({
      mutationKey: ["deletedData", diaryId],
      mutationFn: (diaryId: number) => deleteScrapData(diaryId),
    });

    const handleClick = () => {
      // 스크랩 되어있으면 취소
      if (isScrapped) {
        deleteScrappedData.mutate(diaryId, {
          onSuccess: () => toggleScrap(diaryId),
        });
      } else {
        // 스크랩 안되어있으면 스크랩
        addScrap({ ...props, isScrapped: true });
        setScrappedData.mutate(diaryId, {
          onSuccess: () => toggleScrap(diaryId),
        });
        console.log(isScrapped);
      }
    };

    return (
      <section ref={ref} className={styles.cardSec}>
        <h1>{nickname}</h1>
        <div className={styles.cardDate}>{formattedDate(scrappedAt)}</div>
        <div className={styles.cardContent}>{content}</div>
        <button className={styles.cardBtn}>
          <FontAwesomeIcon
            icon={isScrappedDiary(diaryId) ? fasFaStar : farFaStar}
            onClick={handleClick}
          />
        </button>
      </section>
    );
  }
);

ScrapCard.displayName = "ScrapCard";

export default ScrapCard;
