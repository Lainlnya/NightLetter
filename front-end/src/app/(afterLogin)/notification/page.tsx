'use client';

import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import styles from './notification.module.scss';
import { getNotifications } from '@/libs/DiaryApi/getNotifications';
import Loading from '@/app/loading';
import { Messages } from '@/utils/msg';
import { formattedDate } from '@/utils/dateFormat';
import { setNotificationRead } from '@/libs/DiaryApi/setNotificationRead';
import { useRouter } from 'next/navigation';
import { motion, useAnimation, AnimationControls } from 'framer-motion';
import dayjs from 'dayjs';
import { deleteNotificationRead } from '@/libs/DiaryApi/deleteNotification';
import { useEffect, useState } from 'react';

interface Notification {
  notificationId: number;
  type: string;
  created_at: string;
  title: string;
  content: string;
  isRead: boolean;
}

// 삭제
// 추천 사연일 경우 과거의 것은 당일만 확인 가능

// 낙관적 업데이트
const Notification: React.FC = () => {
  const router = useRouter();
  const queryClient = useQueryClient();
  const [notis, setNotis] = useState<Notification[]>([]);
  const [controlsMap, setControlsMap] = useState<Map<number, AnimationControls>>(new Map());
  const { data: notification } = useQuery({ queryKey: ['notification'], queryFn: () => getNotifications() });

  useEffect(() => {
    if (notification) {
      setNotis(notification);
      const newControlsMap = new Map<number, AnimationControls>();
      notification.forEach((noti: Notification) => {
        newControlsMap.set(noti.notificationId, useAnimation());
      });

      setControlsMap(newControlsMap);
    }
  }, [notification]);

  // 알림 표시하기
  const readNotification = useMutation({
    mutationKey: ['ReadNoti'],
    mutationFn: (notificationId: number) => setNotificationRead(notificationId),
    onMutate: async (notificationId) => {
      await queryClient.cancelQueries({ queryKey: ['notification'] });

      const previousData = queryClient.getQueryData(['notification']);
      console.log('notification:', previousData);

      queryClient.setQueryData(['notification'], (old: Notification[]) => {
        return old.map((item) => (item.notificationId === notificationId ? { ...item, isRead: !item.isRead } : item));
      });

      setNotis((noti) =>
        noti.map((item) => (item.notificationId === notificationId ? { ...item, isRead: !item.isRead } : item))
      );

      return () => queryClient.setQueryData(['notification'], previousData);
    },
    onError: (error, notificationId, rollback) => {
      if (rollback) rollback();
      else console.log(error);
    },
    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: ['notification'] });
    },
  });

  const deleteNotification = useMutation({
    mutationKey: ['DeleteNoti'],
    mutationFn: (notificationId: number) => deleteNotificationRead(notificationId),
  });

  // 알림 클릭시 이동
  const readNoti = (noti: Notification) => {
    const { type, notificationId, created_at } = noti;
    // 코멘트
    if (type === 'GPT_COMMENT_ARRIVAL') {
      if (created_at.split('T')[0] === dayjs().format('YYYY-MM-DD')) {
        router.replace('/comment');
      } else router.replace('/');
      // 추천 사연
    } else if (type === 'RECOMMEND_DIARIES_ARRIVAL') {
      if (created_at.split('T')[0] === dayjs().format('YYYY-MM-DD')) {
        router.replace('/stories');
      }
    }
    readNotification.mutate(notificationId);
  };

  const handleDragEnd = async (notificationId: number, event: any, info: any) => {
    const offset = info.offset.x;
    const controls = controlsMap.get(notificationId);

    if (controls) {
      if (offset < -200) {
        await controls.start({ x: -window.innerWidth, opacity: 0, transition: { duration: 0.5 } });
        deleteNotification.mutate(notificationId);
      } else {
        controls.start({ x: 0, transition: { type: 'spring', stiffness: 300 } });
      }
    }
  };

  if (!notis) {
    return <Loading loadingMessage={Messages.LOADING_CARD_INFO} />;
  }

  return (
    <section className={styles.noti}>
      <h1>알림 목록</h1>
      {notis &&
        notis.map((noti: Notification) => {
          const controls = controlsMap.get(noti.notificationId);
          return (
            <motion.div
              key={noti.notificationId}
              className={styles.eachNoti}
              onClick={() => readNoti(noti)}
              dragConstraints={{ left: 0, right: 0 }}
              drag="x"
              onDragEnd={(event, info) => handleDragEnd(noti.notificationId, event, info)}
              animate={controls}
              whileDrag={{ scale: 1.05 }}
            >
              <div className={styles.up}>
                {noti.isRead ? <div></div> : <div className={styles.unRead}></div>}
                <div className={styles.date}>{formattedDate(noti.created_at.split('T')[0])}</div>
              </div>
              <div>{noti.title}</div>
            </motion.div>
          );
        })}
    </section>
  );
};

export default Notification;
