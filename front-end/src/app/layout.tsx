import type { Metadata } from "next";
import "./globals.scss";
import RQProvider from "./_components/RQProvider";
import GNB from "./_components/common/GNB";
import { ReactNode } from "react";

interface LayoutProps {
  children: ReactNode;
  showGNB: boolean;
}

export const metadata: Metadata = {
  title: "밤편지",
  description: "타로카드를 통해 위로를 얻는 다이어리 플랫폼",
  icons: {
    icon: "/favicon.png",
  },
};

export default async function RootLayout({
  children,
  showGNB = true,
}: LayoutProps) {
  return (
    <html lang="ko">
      <body>
        {showGNB && <GNB />}
        <RQProvider>{children}</RQProvider>
      </body>
    </html>
  );
}
