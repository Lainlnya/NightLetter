'use client';

import { ScrapItem } from '@/types/apis';
import styles from '../../(afterLogin)/scrap/scrap.module.scss';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faStar as farFaStar } from '@fortawesome/free-regular-svg-icons';
import { faStar as fasFaStar } from '@fortawesome/free-solid-svg-icons';
import { formattedDate } from '@/utils/dateFormat';
import { forwardRef } from 'react';
import useScrapStore from '@/store/stories';
import Image from 'next/image';
import dayjs from 'dayjs';

const ScrapCard = forwardRef<HTMLDivElement, ScrapItem>((props, ref) => {
  const { diaryId, nickname, content, scrappedAt, imgUrl, onClick } = props;
  const scrappedDate = scrappedAt ? formattedDate(scrappedAt) : formattedDate(dayjs().format('YYYY-MM-DD'));

  const { toggleScrap, isScrappedDiary } = useScrapStore();

  const handleClick = (e: React.MouseEvent<HTMLButtonElement> | React.TouchEvent<HTMLButtonElement>) => {
    e.stopPropagation();
    toggleScrap(diaryId);
  };

  return (
    <section ref={ref} className={styles.cardSec} onClick={onClick}>
      <div className={styles.scrapped}>
        <h1>{nickname}</h1>
        <button className={styles.cardBtn} onClick={handleClick}>
          <FontAwesomeIcon icon={isScrappedDiary(diaryId) ? fasFaStar : farFaStar} />
        </button>
      </div>
      <div className={styles.cardDate}>{scrappedDate}</div>
      <div className={styles.cardContent}>{content}</div>
      <Image src={imgUrl} width={40} height={70} alt="card" />
    </section>
  );
});

ScrapCard.displayName = 'ScrapCard';

export default ScrapCard;
