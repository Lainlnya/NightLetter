import type { Metadata } from 'next';
import './globals.scss';
import RQProvider from './_components/RQProvider';
import { config } from '@fortawesome/fontawesome-svg-core';
import '@fortawesome/fontawesome-svg-core/styles.css';
config.autoAddCss = false;

export const metadata: Metadata = {
  title: '밤편지',
  description: '타로카드를 통해 위로를 얻는 다이어리 플랫폼',
  icons: {
    icon: '/favicon.png',
  },
};

export default async function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
      <body>
        <RQProvider>{children}</RQProvider>
        <div id="modal-root"></div>
      </body>
    </html>
  );
}
