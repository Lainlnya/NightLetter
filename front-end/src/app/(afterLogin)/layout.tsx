'use client';

import { WebSocketProvider } from '@/context/WebSocketClient';
import GNB from '../_components/common/GNB';
import styles from './layout.module.scss';
import Portal from '../_components/modal/ModalPortal';
import Notification from '../_components/modal/Notification';

export default function AfterLoginLayout({ children }: { children: React.ReactNode }) {
  return (
    <>
      <WebSocketProvider>
        <main className={styles.main}>
          {children}
          <GNB />
        </main>
        <Portal>
          <Notification notification={''} />
        </Portal>
      </WebSocketProvider>
    </>
  );
}
