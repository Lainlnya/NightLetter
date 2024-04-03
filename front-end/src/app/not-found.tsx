import React from "react";
import styles from "./not-found.module.scss";
import Link from "next/link";

const NotFound = () => {
  return (
    <main className={styles.main}>
      <h1>요청하신 페이지는 존재하지 않습니다.</h1>
      <Link href="/">메인 페이지로 이동하기</Link>
    </main>
  );
};

export default NotFound;
