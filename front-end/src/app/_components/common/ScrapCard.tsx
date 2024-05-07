import { ScarpCardResponseBody } from '@/types/apis';
import styles from '../../(afterLogin)/scrap/scrap.module.scss';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faStar as fasFaStar } from '@fortawesome/free-regular-svg-icons';
import { faStar as farFaStar } from '@fortawesome/free-solid-svg-icons';
import { formattedDate } from '@/utils/dateFormat';

const ScrapCard = ({ diaryId, nickname, content, imgUrl, scrappedAt }: ScarpCardResponseBody) => {
  return (
    <section className={styles.cardSec}>
      <h1>{nickname}</h1>
      <div className={styles.cardDate}>{formattedDate(scrappedAt)}</div>
      <div className={styles.cardContent}>{content}</div>
      <button className={styles.cardBtn}>
        <FontAwesomeIcon icon={farFaStar} />
      </button>
    </section>
  );
};

export default ScrapCard;
