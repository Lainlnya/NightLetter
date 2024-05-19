'use client';

import styles from './card.module.scss';
import { getTarotCard } from '@/libs/getTarotCard';
import Loading from '@/app/loading';
import { useEffect, useState } from 'react';
import { ReadonlyURLSearchParams, useRouter, useSearchParams } from 'next/navigation';
import Image from 'next/image';
import { Messages } from '@/utils/msg';
import { useQuery } from '@tanstack/react-query';
import tarot_background from '../../../../public/images/tarot-background.png';
import { CardInfo } from '@/types/card';

const ViewCard: React.FC = () => {
  const [card, setCard] = useState<CardInfo>();
  const [isBack, setIsBack] = useState<boolean>(true);
  const [isLoadingCustom, setIsLoadingCustom] = useState<boolean>(true);

  const router = useRouter();
  const searchParams: ReadonlyURLSearchParams = useSearchParams();

  const { isLoading, data: pastCard } = useQuery({
    queryKey: ['PastTarotCard'],
    queryFn: () => getTarotCard(searchParams.get('info') as string, 'POST'),
    enabled: searchParams.get('info') === 'past',
  });

  const { data: presentCard } = useQuery({
    queryKey: ['PresentTarotCard'],
    queryFn: () => getTarotCard(searchParams.get('info') as string, 'GET'),
    enabled: searchParams.get('info') === 'present',
  });

  const { data: futureCard } = useQuery({
    queryKey: ['FutureTarotCard'],
    queryFn: () => getTarotCard(searchParams.get('info') as string, 'GET'),
    enabled: searchParams.get('info') === 'future',
  });

  useEffect(() => {
    let timer: NodeJS.Timeout | number | null = null;

    const updateCardData = (cardData: CardInfo) => {
      setCard(cardData);

      timer = setTimeout(() => {
        setIsLoadingCustom(false);
      }, 1500);
    };

    if (pastCard && searchParams.get('info') === 'past') {
      updateCardData(pastCard);
    }

    if (presentCard && searchParams.get('info') === 'present') {
      updateCardData(presentCard);
    }

    if (futureCard && searchParams.get('info') === 'future') {
      updateCardData(futureCard);
    }

    return () => {
      if (timer) {
        clearTimeout(timer);
      }
    };
  }, [pastCard, presentCard, futureCard]);

  if (!card || isLoading || isLoadingCustom) {
    return <Loading loadingMessage={Messages.LOADING_CARD_INFO} />;
  }

  return (
    card && (
      <section className={styles.main}>
        {searchParams.get('info') === 'present' && <div className={styles.today}>오늘의 감정을 나타내는 카드는...</div>}
        <div>{card.name}</div>
        <div className={`${styles.card} ${isBack ? styles.isRight : ''}`}>
          <Image
            className={styles.front}
            src={card?.imgUrl || tarot_background}
            alt="tarot image"
            width={230}
            height={400}
          />
          <div>
            <Image
              className={styles.back}
              onClick={() => setIsBack(!isBack)}
              src={card?.imgUrl || tarot_background}
              alt="tarot image"
              width={230}
              height={400}
            />
            <div className={`${isBack ? styles.nonView : styles.view}`} onClick={() => setIsBack(!isBack)}>
              {card.desc}
            </div>
          </div>
        </div>
        <div className={styles.meaning}>{card.keyword}</div>
        {searchParams.get('info') === 'past' && (
          <button className={styles.comment} onClick={() => router.push('/')}>
            다이어리 쓰러가기
          </button>
        )}
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
      </section>
    )
  );
};

export default ViewCard;
