"use client";

import { Suspense, useEffect, useState } from "react";
import styles from "./chatting.module.scss";
import Link from "next/link";
import { motion, useMotionValue } from "framer-motion";
import Loading from "@/app/loading";
import { Messages } from "@/utils/msg";
import { useQuery } from "@tanstack/react-query";
import getTodayCardInfo from "@/libs/ChatApis/getTodayCardInfo";
import Image from "next/image";

interface ChatRoom {
  cardNo: number;
  cardName: string;
  cardImgUrl: string;
  cardDir: string;
  cardType: string;
}

const Chatting: React.FC = () => {
  // 오늘의 카드를 뽑지 않으면 채팅방 이용할 수 없음
  const [chatRoomList, setChatRoomList] = useState<ChatRoom[]>();
  const { data: todayCard } = useQuery({
    queryKey: ["todayCard"],
    queryFn: () => getTodayCardInfo(),
  });

  useEffect(() => {
    if (todayCard) {
      const { pastCard, nowCard, futureCard } = todayCard;
      setChatRoomList([pastCard, nowCard, futureCard]);
    }
  }, [todayCard]);

  return (
    <Suspense
      fallback={<Loading loadingMessage={Messages.LOADING_CARD_INFO} />}
    >
      <section className={styles.chatting}>
        <div>
          <Link href={`/chatting/room?roomId=1`}>1번 채팅방</Link>
        </div>
        <ul className={styles.chatRoom}>
          {chatRoomList &&
            chatRoomList?.map((chatRoom) => (
              <div key={chatRoom.cardNo}>
                <li>
                  <Link href={`/chatting/room?roomId=${chatRoom.cardNo}`}>
                    <motion.div drag='x'>
                      <Image
                        src={chatRoom.cardImgUrl}
                        alt={chatRoom.cardName}
                        width={150}
                        height={270}
                      />
                    </motion.div>
                  </Link>
                </li>
              </div>
            ))}
        </ul>
      </section>
    </Suspense>
  );
};

export default Chatting;
