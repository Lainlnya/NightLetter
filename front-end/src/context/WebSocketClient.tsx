'use client';

import { createContext, useContext, useEffect, useState } from 'react';
import { Client, IMessage } from '@stomp/stompjs';

const WebSocketContext = createContext<Client | null>(null);

export const WebSocketProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [client, setClient] = useState<Client | null>(null);

  useEffect(() => {
    const client = new Client({
      brokerURL: 'wss://letter-for.me/ws-stomp',
      reconnectDelay: 5000,
      debug: (str) => {
        console.log('STOMP: ' + str);
      },
      onStompError: (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
      },
      onWebSocketClose: (event) => {
        console.warn('WebSocket closed: ', event);
      },
      onConnect: () => {
        console.log('Connected to WebSocket');
      },
    });

    client.activate();
    setClient(client);

    return () => {
      client.deactivate();
    };
  }, []);

  return <WebSocketContext.Provider value={client}>{children}</WebSocketContext.Provider>;
};

export const useWebSocket = (): Client | null => {
  return useContext(WebSocketContext);
};
