import { useWebSocket } from '@/context/WebSocketClient';
import { IMessage } from '@stomp/stompjs';
import React, { ReactElement, useEffect, useRef, useState } from 'react';
import { createPortal } from 'react-dom';

interface PortalProps {
  children: ReactElement;
}

const Portal = ({ children }: PortalProps) => {
  const [mounted, setMounted] = useState<boolean>(false);
  const [updatedChildren, setUpdatedChildren] = useState<ReactElement>(children);

  useEffect(() => {
    console.log(mounted);
    setMounted(true);
    return () => setMounted(false);
  }, []);

  // 웹소켓 연결부분
  const client = useWebSocket();
  const subscriptionRef = useRef<any>(null);
  const [notification, setNotification] = useState<string>('');

  useEffect(() => {
    if (!client) {
      console.log('client');
      return;
    }
    const onConnect = () => {
      const subscription = client.subscribe(`/user/notification`, (message: IMessage) => {
        if (message.body) {
          setNotification(message.body);
        }
      });
      subscriptionRef.current = subscription;
    };

    if (client.connected) {
      onConnect();
    } else {
      client.onConnect = onConnect;
    }
    return () => {
      if (subscriptionRef.current) {
        subscriptionRef.current.unsubscribe();
      }
    };
  }, [client, notification]);
  // 웹소켓 연결 끝

  useEffect(() => {
    // notification이 변경될 때마다 updatedChildren을 업데이트합니다.
    setUpdatedChildren(React.cloneElement(children, { notification }));
  }, [notification, children]);

  if (typeof window === 'undefined') return <></>;

  return mounted ? createPortal(updatedChildren, document.getElementById('modal-root') as HTMLElement) : <></>;
};

export default Portal;
