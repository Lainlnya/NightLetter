import { ScrapItem } from "@/types/apis";
import styles from "../../(afterLogin)/scrap/scrap.module.scss";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faStar as fasFaStar } from "@fortawesome/free-regular-svg-icons";
import { faStar as farFaStar } from "@fortawesome/free-solid-svg-icons";
import { formattedDate } from "@/utils/dateFormat";
import { forwardRef } from "react";

const ScrapCard = forwardRef<HTMLDivElement, ScarpCardResponseBody>(
  (props: ScrapItem, ref) => {
    const { diaryId, nickname, content, imgUrl, scrappedAt }: ScrapItem = props;
    return (
      <section ref={ref} className={styles.cardSec}>
        <h1>{nickname}</h1>
        <div className={styles.cardDate}>{formattedDate(scrappedAt)}</div>
        <div className={styles.cardContent}>{content}</div>
        <button className={styles.cardBtn}>
          <FontAwesomeIcon icon={farFaStar} />
        </button>
      </section>
    );
  }
);

ScrapCard.displayName = "ScrapCard";

export default ScrapCard;
