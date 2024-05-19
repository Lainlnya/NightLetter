'use client';

import { createContext, useContext, useEffect, useState } from 'react';
import { Client, IMessage } from '@stomp/stompjs';

interface WebSocketContextType {
  client: Client | null;
  username: number;
}

const WebSocketContext = createContext<WebSocketContextType | null>(null);

export const WebSocketProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [client, setClient] = useState<Client | null>(null);
  const [username, setUsername] = useState<number>(0);

  useEffect(() => {
    const client = new Client({
      brokerURL: 'wss://letter-for.me/ws-stomp',
      reconnectDelay: 5000,
      debug: (str) => {
        console.log('STOMP: ' + str);
        const userNameMatch = str.match(/user-name:(\d+)/);
        if (userNameMatch) {
          setUsername(parseInt(userNameMatch[1]));
        }
      },
      onStompError: (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
      },
      onWebSocketClose: (event) => {
        console.warn('WebSocket closed: ', event);
      },
      onConnect: (frame) => {
        const principle = JSON.parse(frame.headers['user-name']);
        setUsername(principle);
        console.log('username' + username);
        console.log('Connected to WebSocket');
      },
    });

    client.activate();
    setClient(client);

    return () => {
      client.deactivate();
    };
  }, []);

  return <WebSocketContext.Provider value={{ client, username }}>{children}</WebSocketContext.Provider>;
};

export const useWebSocket = (): WebSocketContextType | null => {
  return useContext(WebSocketContext);
};
