'use client';

import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import styles from './notification.module.scss';
import { getNotifications } from '@/libs/DiaryApi/getNotifications';
import { setNotificationRead } from '@/libs/DiaryApi/setNotificationRead';
import { deleteNotificationRead } from '@/libs/DiaryApi/deleteNotification';
import Loading from '@/app/loading';
import { Messages } from '@/utils/msg';
import { formattedDate } from '@/utils/dateFormat';
import { useRouter } from 'next/navigation';
import dayjs from 'dayjs';
import { useEffect, useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faClose } from '@fortawesome/free-solid-svg-icons';
import Toast from '@/app/_components/post/Toast';

interface Notification {
  notificationId: number;
  type: string;
  created_at: string;
  title: string;
  content: string;
  isRead: boolean;
}

const Notification: React.FC = () => {
  const router = useRouter();
  const queryClient = useQueryClient();
  const { data: notification } = useQuery({ queryKey: ['notification'], queryFn: () => getNotifications() });
  const [notis, setNotis] = useState<Notification[]>([]);
  const [toast, setToast] = useState<boolean>(false);

  useEffect(() => {
    if (notification) {
      setNotis(notification);
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
    onMutate: async (notificationId) => {
      await queryClient.cancelQueries({ queryKey: ['notification'] });

      const previousData = queryClient.getQueryData(['notification']);
      console.log('notification:', previousData);

      queryClient.setQueryData(['notification'], (old: Notification[]) => {
        return old.filter((item) => item.notificationId !== notificationId);
      });

      setNotis((noti) => noti.filter((item) => item.notificationId !== notificationId));

      return { previousData };
    },
    onError: (error, notificationId, context) => {
      if (context?.previousData) {
        queryClient.setQueryData(['notification'], context.previousData);
      } else {
        console.log(error);
      }
    },
    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: ['notification'] });
    },
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
      } else {
        setToast(true);
      }
    }
    readNotification.mutate(notificationId);
  };

  const deleteNoti = (event: any, notificationId: number) => {
    event.preventDefault();
    event.stopPropagation();
    deleteNotification.mutate(notificationId);
  };

  if (!notis) {
    return <Loading loadingMessage={Messages.LOADING_CARD_INFO} />;
  }

  return (
    <section className={styles.noti}>
      <h1>알림 목록</h1>
      {notis &&
        notis.map((noti: Notification) => (
          <div key={noti.notificationId} className={styles.eachNoti} onClick={() => readNoti(noti)}>
            <div className={styles.up}>
              {noti.isRead ? <div></div> : <div className={styles.unRead}></div>}
              <div onClick={(e) => deleteNoti(e, noti.notificationId)}>
                <FontAwesomeIcon icon={faClose} />
              </div>
            </div>
            <div>{noti.title}</div>
            <div className={styles.date}>{formattedDate(noti.created_at.split('T')[0])}</div>
          </div>
        ))}
      {toast && <Toast text={Messages.DIARY_NOSTORIES} setToast={setToast} />}
      {notis && notis.length === 0 && <div className={styles.noAlarm}>알림 목록이 없습니다</div>}
    </section>
  );
};

export default Notification;
