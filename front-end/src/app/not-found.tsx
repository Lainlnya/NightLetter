import React from "react";
import styles from "./not-found.module.scss";
import Link from "next/link";
import Image from "next/image";
import tarotBackground from "../../public/images/tarot-background.png";

const NotFound = () => {
  return (
    <main className={styles.main}>
      <Image
        className={styles.tarot}
        src={tarotBackground}
        alt="tarot image"
        width={200}
        height={360}
      />
      <h1>요청하신 페이지는 존재하지 않습니다.</h1>
      <Link className={styles.redirect} href="/">
        메인 페이지로 이동하기
      </Link>
    </main>
  );
};

export default NotFound;
