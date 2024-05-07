'use client';

import { useQuery } from '@tanstack/react-query';
import styles from './scrap.module.scss';
import { getScrapData } from '@/libs/DiaryApis';
import ScrapCard from '@/app/_components/common/ScrapCard';
import { useEffect } from 'react';
import { ScarpCardResponseBody } from '@/types/apis';

const Scrap: React.FC = () => {
  const { isLoading, data: scrapList } = useQuery({ queryKey: ['ScrapList'], queryFn: () => getScrapData() });

  useEffect(() => {
    console.log(scrapList);
  }, [scrapList]);
  return (
    scrapList && (
      <section className={styles.scrap}>
        {scrapList.content.map((card: ScarpCardResponseBody) => (
          <ScrapCard
            diaryId={card.diaryId}
            nickname={card.nickname}
            scrappedAt={card.scrappedAt}
            content={card.content}
            imgUrl={card.imgUrl}
          />
        ))}
      </section>
    )
  );
};

export default Scrap;
