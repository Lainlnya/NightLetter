'use client';

import styles from './room.module.scss';
import { useSearchParams } from 'next/navigation';
import { useEffect, useRef, useState } from 'react';
import { Client, IMessage } from '@stomp/stompjs';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faAngleLeft } from '@fortawesome/free-solid-svg-icons';
import { convertTime } from '@/utils/dateFormat';
import Link from 'next/link';
import { v4 as uuidv4 } from 'uuid';

import { ChatMessageRequest, ChatMessageResponse } from '@/types/chat';
import { MyChat, OthersChat, OthersChatWithThumbnail } from '@/app/_components/chatting/ChatBlock';
import SendButton from '@/app/_components/chatting/SendButton';
import { useInfiniteQuery } from '@tanstack/react-query';
import getChatHistory from '@/libs/ChatApis/getChatHistory';
import { useIntersectionObserver } from '@/libs/ChatApis/useIntersectionObserver';

export default function Room() {
  const searchParams = useSearchParams();
  const roomId = searchParams.get('roomId');
  const cardState = searchParams.get('state');
  const [stompClient, setStompClient] = useState<Client | null>(null);
  const [messages, setMessages] = useState<ChatMessageResponse[]>([]);
  const [newMessage, setNewMessage] = useState<string>('');

  const {
    data: chatHistory,
    fetchNextPage,
    hasNextPage,
  } = useInfiniteQuery({
    queryKey: ['chatHistory', roomId],
    queryFn: ({ pageParam }) => getChatHistory(roomId, pageParam),
    initialPageParam: 0,
    getNextPageParam: (lastPage, allPages) => {
      const nextPage = allPages.length + 1;
      return lastPage.data.length === 0 ? undefined : nextPage;
    },
    select: (data) => ({
      pages: data?.pages.flatMap((page) => page.data).reverse(),
      pageParams: data.pageParams,
    }),
  });

  const { setTarget } = useIntersectionObserver({ hasNextPage, fetchNextPage });
  const messageEndRef = useRef<HTMLDivElement | null>(null);
  const messageContainerRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    if (chatHistory) {
      // 추가된 메시지의 높이를 계산하여 스크롤 위치 유지
      const previousHeight = messageContainerRef.current?.scrollHeight || 0;
      setMessages(chatHistory.pages);
      setTimeout(() => {
        if (messageContainerRef.current) {
          messageContainerRef.current.scrollTop = messageContainerRef.current.scrollHeight - previousHeight;
        }
      }, 0);
    }
    // 웹소켓 연결
    const client = new Client({
      brokerURL: 'wss://dev.letter-for.me/ws-stomp',
      reconnectDelay: 5000,
      onConnect: () => {
        client.subscribe(`/room/${roomId}`, (message: IMessage) => {
          const msg: ChatMessageResponse = JSON.parse(message.body);
          setMessages((prevMessages) => [...prevMessages, msg]);
          console.log(msg);
        });
      },
    });
    client.activate();

    setStompClient(client);

    return () => {
      client.deactivate();
    };
  }, [roomId, chatHistory]);

  useEffect(() => {
    messageEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages.length]);

  // 메세지 보내는 부분
  const sendMessage = () => {
    if (stompClient && newMessage) {
      const chatMessage: ChatMessageRequest = {
        message: newMessage,
      };

      stompClient.publish({
        destination: `/send/${roomId}`,
        body: JSON.stringify(chatMessage),
      });
      setNewMessage('');
    }
  };

  // 메세지 렌더링
  const renderChatting = messages.map((msg: ChatMessageResponse, idx) => {
    const prevChat = idx >= 1 ? messages[idx - 1] : undefined;
    const prevTime = prevChat ? convertTime(prevChat.sendTime) : '';
    const isPrevSender = prevChat ? prevChat.senderId === msg.senderId : false;
    const isSameDate = prevTime === convertTime(msg.sendTime);

    if (idx === messages.length - 1) {
      if (msg.sentByMe) {
        return <MyChat key={uuidv4()} msg={msg} last={true} />;
      }

      if (isPrevSender) {
        return <OthersChat key={uuidv4()} msg={msg} last={true} />;
      }

      return <OthersChatWithThumbnail key={uuidv4()} msg={msg} last={true} />;
    }

    // 채팅 시간 표기
    if (msg.sentByMe) {
      const lastWritten = msg.senderId !== messages[idx + 1].senderId;
      return <MyChat key={uuidv4()} msg={msg} last={lastWritten} />;
    }

    if (msg.senderId !== messages[idx + 1].senderId) {
      return <OthersChat key={uuidv4()} msg={msg} last={true} />;
    }

    if (isPrevSender) {
      // 같은 날짜가 아니다
      if (isSameDate === false && convertTime(msg.sendTime) !== convertTime(messages[idx + 1].sendTime)) {
        return <OthersChat key={uuidv4()} msg={msg} last={true} />;
      } else return <OthersChat key={uuidv4()} msg={msg} last={false} />;
    }

    return <OthersChatWithThumbnail key={uuidv4()} msg={msg} last={isSameDate === false} />;
  });

  return (
    <section className={styles.roomSection}>
      <nav className={styles.nav}>
        <Link href="/chatting" replace>
          <FontAwesomeIcon icon={faAngleLeft} />
        </Link>
        <div>{cardState === '0' ? '과거' : cardState === '1' ? '현재' : '미래'}의 방</div>
        <div></div>
      </nav>
      <section className={styles.chattingArea} ref={messageContainerRef}>
        {messages.length === 0 ? (
          <div className={styles.noMessage}>첫 메세지를 보내보세요!</div>
        ) : (
          <>
            <div ref={setTarget}></div>
            <section>{renderChatting}</section>
            <div ref={messageEndRef}></div>
          </>
        )}
      </section>
      <section className={styles.sendSection}>
        <SendButton newMessage={newMessage} setNewMessage={setNewMessage} sendMessage={sendMessage} />
      </section>
    </section>
  );
}
