'use client';

import styles from './room.module.scss';
import Image from 'next/image';
import { useSearchParams } from 'next/navigation';
import { useEffect, useState } from 'react';
import { Client, IMessage } from '@stomp/stompjs';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faAngleLeft, faArrowUp } from '@fortawesome/free-solid-svg-icons';
import { convertTime } from '@/utils/dateFormat';
import Link from 'next/link';

interface ChatMessageRequest {
  message: string;
}

interface ChatMessageResponse {
  senderId: number;
  profileImageUrl: string;
  sendTime: string;
  nickname: string;
  message: string;
  sentByMe: boolean;
}

export default function Room() {
  const searchParams = useSearchParams();
  const roomId = searchParams.get('roomId');
  const cardState = searchParams.get('state');
  const [stompClient, setStompClient] = useState<Client | null>(null);
  const [messages, setMessages] = useState<ChatMessageResponse[]>([]);
  const [newMessage, setNewMessage] = useState<string>('');
  const [prevInfo, setPrevInfo] = useState({ name: '', time: '' });

  useEffect(() => {
    // 과거 채팅 히스토리
    const loadChatHistory = async () => {
      try {
      } catch (error) {
        console.log(error);
      }
    };
    loadChatHistory();

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
  }, [roomId]);

  const sendMessage = () => {
    if (stompClient && newMessage) {
      const chatMessage: ChatMessageRequest = {
        message: newMessage,
      };

      stompClient.publish({
        destination: `/send/${roomId}`,
        body: JSON.stringify(chatMessage),
      });

      console.log(messages);
      setNewMessage('');
    }
  };

  const identifySamePerson = (name: string, time: string) => {
    const convertedTime = convertTime(time);
    // if (name === prevInfo.name && convertedTime === prevInfo.time) {
    if (convertedTime === prevInfo.time) {
      return 'same';
      // } else if (name === prevInfo.name && convertedTime !== prevInfo.time) {
    } else if (convertedTime !== prevInfo.time) {
      return 'time';
    } else {
      setPrevInfo({ name, time: convertedTime });
      return 'diff';
    }
  };

  const isLastMessageOfSameTimeGroup = (index: number) => {
    if (index === messages.length - 1) return true;
    const currentMessageTime = convertTime(messages[index].sendTime);
    const nextMessageTime = convertTime(messages[index + 1].sendTime);
    return currentMessageTime !== nextMessageTime;
  };

  return (
    <section className={styles.roomSection}>
      <nav className={styles.nav}>
        <Link href="/chatting" replace>
          <FontAwesomeIcon icon={faAngleLeft} />
        </Link>
        <div>{cardState === '0' ? '과거' : cardState === '1' ? '현재' : '미래'}의 방</div>
        <div></div>
      </nav>
      <section className={styles.chattingArea}>
        {messages.map((msg, idx) => {
          const result = identifySamePerson(msg.nickname, msg.sendTime);
          const showNickName = result === 'diff';
          const showTime = isLastMessageOfSameTimeGroup(idx);

          return (
            <div key={idx} className={`${styles.message} ${msg.sentByMe === true ? styles.meMsg : ''}`}>
              <Image
                className={`${styles.profileImg} ${msg.sentByMe === true ? styles.me : ''}`}
                src={'https://ssafy-tarot-01.s3.ap-northeast-2.amazonaws.com/profile/1.jpg'}
                width={40}
                height={40}
                alt="profile"
              />
              <div className={styles.msgBox}>
                {showNickName && (
                  <div className={`${styles.nickname} ${msg.sentByMe === true ? styles.me : ''}`}>{msg.nickname}</div>
                )}
                <div>
                  <span className={styles.msg}>{msg.message}</span>
                  {(showTime || msg.sentByMe) && <span>{convertTime(msg.sendTime)}</span>}
                </div>
              </div>
            </div>
          );
        })}
      </section>
      <section className={styles.sendSection}>
        <input
          style={{ color: 'black' }}
          type="text"
          value={newMessage}
          onChange={(e) => setNewMessage(e.target.value)}
        />
        <button onClick={sendMessage}>
          <FontAwesomeIcon icon={faArrowUp} />
        </button>
      </section>
    </section>
  );
}
