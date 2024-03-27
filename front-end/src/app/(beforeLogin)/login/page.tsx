"use client";

import styles from "./login.module.scss";

const Login = () => {
  const handleClick = () => {
    window.location.href = `http://letter-for.me/api/v1/auth/oauth2/kakao`;
  };

  return (
    <>
      <main className={styles.main}>
        <h1>밤편지</h1>
        <div>당신의 하루를 기록해보세요</div>
        <img onClick={handleClick} src="/images/kakao-login-large-narrow.png" />
      </main>
    </>
  );
};

export default Login;
