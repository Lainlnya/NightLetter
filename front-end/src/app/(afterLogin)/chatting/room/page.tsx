"use client";

import styles from "./room.module.scss";
import { useSearchParams } from "next/navigation";
import { useEffect, useState } from "react";
import { Client, IMessage } from "@stomp/stompjs";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faAngleLeft } from "@fortawesome/free-solid-svg-icons";
import { convertTime } from "@/utils/dateFormat";
import Link from "next/link";
import { v4 as uuidv4 } from "uuid";

import { ChatMessageRequest, ChatMessageResponse } from "@/types/chat";
import {
  MyChat,
  OthersChat,
  OthersChatWithThumbnail,
} from "@/app/_components/chatting/ChatBlock";
import SendButton from "@/app/_components/chatting/SendButton";
import { useInfiniteQuery } from "@tanstack/react-query";
import getChatHistory from "@/libs/ChatApis/getChatHistory";

export default function Room() {
  const searchParams = useSearchParams();
  const roomId = searchParams.get("roomId");
  const cardState = searchParams.get("state");
  const [stompClient, setStompClient] = useState<Client | null>(null);
  const [messages, setMessages] = useState<ChatMessageResponse[]>([]);
  const [newMessage, setNewMessage] = useState<string>("");

  // const { data, fetchNextPage, hasNextPage } = useInfiniteQuery({
  //   queryKey: ["chatHistory"],
  //   queryFn: ({ pageParam = 0 }) => getChatHistory(roomId, pageParam),
  //   getNextPageParam: (lastPage, allPages) => true,
  //   },
  // });
  useEffect(() => {
    // 과거 채팅 히스토리
    const loadChatHistory = () => {};
    loadChatHistory();

    // 웹소켓 연결
    const client = new Client({
      brokerURL: "wss://dev.letter-for.me/ws-stomp",
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

      console.log(messages);
      setNewMessage("");
    }
  };

  // 메세지 렌더링
  const renderChatting = messages.map((msg: ChatMessageResponse, idx) => {
    const prevChat = idx >= 1 ? messages[idx - 1] : undefined;
    const prevTime = prevChat ? convertTime(prevChat.sendTime) : "";
    const isPrevSender = prevChat ? prevChat.senderId === msg.senderId : false;
    const isSameDate = prevTime === convertTime(msg.sendTime);

    if (idx === messages.length - 1) {
      if (msg.sentByMe) {
        return <MyChat key={uuidv4()} msg={msg} last={true} />;
      }

      if (isPrevSender && isSameDate) {
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

    if (isPrevSender && isSameDate && convertTime(msg.sendTime) === prevTime) {
      return <OthersChat key={uuidv4()} msg={msg} last={false} />;
    }

    return <OthersChatWithThumbnail key={uuidv4()} msg={msg} last={false} />;
  });

  return (
    <section className={styles.roomSection}>
      <nav className={styles.nav}>
        <Link href='/chatting' replace>
          <FontAwesomeIcon icon={faAngleLeft} />
        </Link>
        <div>
          {cardState === "0" ? "과거" : cardState === "1" ? "현재" : "미래"}의
          방
        </div>
        <div></div>
      </nav>
      <section className={styles.chattingArea}>{renderChatting}</section>
      <section className={styles.sendSection}>
        <SendButton
          newMessage={newMessage}
          setNewMessage={setNewMessage}
          sendMessage={sendMessage}
        />
      </section>
    </section>
  );
}
