'use client';

import styles from './card.module.scss';
import { getTarotCard } from '@/_apis/TarotApis';
import Loading from '@/app/loading';
import { useEffect, useState } from 'react';
import { ReadonlyURLSearchParams, useRouter, useSearchParams } from 'next/navigation';

interface CardInfo {
  cardNo: number;
  cardName: string;
  cardImgUrl: string;
  cardKeyWord: string;
  cardDesc: string;
}

const ViewCard: React.FC = () => {
  const [card, setCard] = useState<CardInfo>();
  const [isBack, setIsBack] = useState<boolean>(true);

  const router = useRouter();
  const searchParams: ReadonlyURLSearchParams = useSearchParams();

  useEffect(() => {
    const presentCardInfo = sessionStorage.getItem('presentCardInfo');
    if (searchParams.get('info') === 'present' && presentCardInfo !== null) {
      const { cardNo, cardName, cardImgUrl, cardKeyWord, cardDesc } = JSON.parse(presentCardInfo);
      setCard({ cardNo, cardName, cardImgUrl, cardKeyWord, cardDesc });
    }
    // setCard(getTarotCard);
  }, []);

  if (!card) {
    return <Loading loadingMessage='로딩중입니다.' />;
  }
  return (
    card && (
      <main className={styles.main}>
        {searchParams.get('info') === 'present' && <div className={styles.today}>오늘의 감정을 나타내는 카드는...</div>}
        <div>{card.cardName}</div>
        <div className={`${styles.card} ${isBack ? styles.isRight : ''}`}>
          <img className={styles.front} src={`/deleted_card/${card.cardNo}.png`} alt='tarot image' />
          {/* <img className={styles.front} src={card.cardImgUrl} alt='tarot image' /> */}
          <div>
            <img
              className={styles.back}
              onClick={() => setIsBack(!isBack)}
              src={`/deleted_card/${card.cardNo}.png`}
              alt='tarot image'
            />
            {/* <img className={styles.back} onClick={() => setIsBack(!isBack)} src={card.cardImgUrl} alt='tarot image' /> */}
            <div className={`${isBack ? styles.nonView : styles.view}`} onClick={() => setIsBack(!isBack)}>
              {card.cardKeyWord}
            </div>
          </div>
        </div>
        <div className={styles.meaning}>{card.cardDesc}</div>
        {searchParams.get('info') === 'present' && (
          <button className={styles.comment} onClick={() => router.push('/tarot?info=future')}>
            미래카드 뽑기
          </button>
        )}
        {searchParams.get('info') === 'future' && (
          <button className={styles.comment} onClick={() => router.push('/comment')}>
            코멘트 보러가기
          </button>
        )}
      </main>
    )
  );
};

export default ViewCard;
