"use client";

import styles from "./room.module.scss";
import { useSearchParams } from "next/navigation";
import { useEffect, useState } from "react";
import { Client, IMessage } from "@stomp/stompjs";

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
  const roomId = searchParams.get("roomId");
  const [stompClient, setStompClient] = useState<Client | null>(null);
  const [messages, setMessages] = useState<ChatMessageResponse[]>([]);
  const [newMessage, setNewMessage] = useState<string>("");

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
      brokerURL: "wss://dev.letter-for.me/ws-stomp",
      reconnectDelay: 5000,
      onConnect: () => {
        client.subscribe(`/room/${roomId}`, (message: IMessage) => {
          const msg: ChatMessageResponse = JSON.parse(message.body);
          setMessages((prevMessages) => [...prevMessages, msg]);
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
      setNewMessage("");
    }
  };

  return (
    <section className={styles.roomSection}>
      <div>
        {messages.map((msg, idx) => (
          <div key={idx}>
            {msg.nickname}님 {msg.message}
          </div>
        ))}
      </div>
      <div>
        <input
          style={{ color: "black" }}
          type='text'
          value={newMessage}
          onChange={(e) => setNewMessage(e.target.value)}
        />
        <button onClick={sendMessage}>Send</button>
      </div>
    </section>
  );
}
