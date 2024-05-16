'use client';

import { Suspense, useEffect, useState } from 'react';
import styles from './chatting.module.scss';
import Link from 'next/link';
import { motion, useAnimation, useMotionValue } from 'framer-motion';
import Loading from '@/app/loading';
import { Messages } from '@/utils/msg';
import { useQuery } from '@tanstack/react-query';
import getTodayCardInfo from '@/libs/ChatApis/getTodayCardInfo';
import Image from 'next/image';

interface ChatRoom {
  cardNo: number;
  cardName: string;
  cardImgUrl: string;
  cardDir: string;
  cardType: string;
}

const Chatting: React.FC = () => {
  // 오늘의 카드를 뽑지 않으면 채팅방 이용할 수 없음
  const [chatRoomList, setChatRoomList] = useState<ChatRoom[]>([]);
  const [chatRoomIndex, setChatRoomIndex] = useState<number[]>([]);
  const [currentIndex, setCurrentIndex] = useState<number>(1);
  const { data: todayCard } = useQuery({
    queryKey: ['todayCard'],
    queryFn: () => getTodayCardInfo(),
  });

  const x = useMotionValue(0);
  const controls = useAnimation();

  useEffect(() => {
    if (todayCard) {
      const { pastCard, nowCard, futureCard } = todayCard;
      if (pastCard && nowCard && futureCard) {
        setChatRoomList([pastCard, nowCard, futureCard]);
        setChatRoomIndex([pastCard.cardNo, nowCard.cardNo, futureCard.cardNo]);
      }
      console.log(todayCard);
    }
  }, [todayCard]);

  const updateOrder = (direction: number) => {
    setChatRoomList((prev) => {
      const newOrder = [...prev];
      if (direction === 1) {
        newOrder.push(newOrder.shift()!);
      } else if (direction === -1) {
        newOrder.unshift(newOrder.pop()!);
      }
      return newOrder;
    });
    setCurrentIndex((prev) => (prev + direction + chatRoomList.length) % chatRoomList.length);
  };

  const handleDragEnd = (event: any, info: any) => {
    const offset = info.offset.x;
    const threshold = 100;
    if (offset > threshold) {
      controls.start({ x: 0 });
      // 오른쪽
      updateOrder(-1);
    } else if (offset < -threshold) {
      controls.start({ x: 0 });
      // 왼쪽
      updateOrder(1);
    } else {
      controls.start({ x: 0 });
    }
  };

  return (
    <Suspense fallback={<Loading loadingMessage={Messages.LOADING_CARD_INFO} />}>
      <section className={styles.chatting}>
        {chatRoomList.length === 0 ? (
          <section className={styles.noCard}>
            <div>
              아직 일기를 작성하지 않으셨네요! <br /> 오늘의 일기를 작성해보는건 어떨까요 ? <br /> 일기를 쓰시면 오늘의
              채팅에 참여할 수 있어요!
            </div>
            <Link href="/post" replace>
              <button className={styles.goPost}>일기 쓰러 가기</button>
            </Link>
          </section>
        ) : (
          <motion.div
            drag="x"
            dragConstraints={{ left: 0, right: 0 }}
            onDragEnd={handleDragEnd}
            style={{ x }}
            animate={controls}
          >
            <ul className={styles.chatRoom}>
              {chatRoomList &&
                chatRoomList?.map((chatRoom, index) => {
                  const isCenter = index === 1;
                  const scale = isCenter ? 1.3 : 0.8;
                  const blur = isCenter ? 0 : 5;

                  return (
                    <motion.li
                      className={styles.motionLi}
                      key={chatRoom.cardNo}
                      style={{ scale, filter: `blur(${blur}px)` }}
                    >
                      <Link
                        href={{
                          pathname: `/chatting/room`,
                          query: {
                            roomId: chatRoom.cardNo,
                            state: chatRoomIndex.findIndex((value) => value === chatRoom.cardNo),
                          },
                        }}
                      >
                        <motion.div className={styles.card}>
                          {chatRoom.cardNo === chatRoomIndex[0] && <div>과거의 방</div>}
                          {chatRoom.cardNo === chatRoomIndex[1] && <div>현재의 방</div>}
                          {chatRoom.cardNo === chatRoomIndex[2] && <div>미래의 방</div>}
                          <Image src={chatRoom.cardImgUrl} alt={chatRoom.cardName} width={150} height={270} />
                          {index === 1 && <button className={styles.goChat}>채팅방 들어가기</button>}
                        </motion.div>
                      </Link>
                    </motion.li>
                  );
                })}
            </ul>
          </motion.div>
        )}
      </section>
    </Suspense>
  );
};

export default Chatting;
