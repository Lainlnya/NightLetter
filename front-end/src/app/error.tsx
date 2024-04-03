"use client";
import Link from "next/link";
import styles from "./error.module.scss";
import React from "react";

const ErrorPage = () => {
  return (
    <main className={styles.main}>
      <h1>예상치 못한 오류가 발생했습니다.</h1>
      <Link href="/">메인 페이지로 이동하기</Link>
    </main>
  );
};

export default ErrorPage;
