import type { Metadata } from "next";
import "./globals.scss";
import RQProvider from "./_components/RQProvider";
import React from "react";

export const metadata: Metadata = {
  title: "밤편지",
  description: "타로카드를 통해 위로를 얻는 다이어리 플랫폼",
  icons: {
    icon: "/favicon.png",
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
      </body>
    </html>
  );
}
