import Image from "next/image";
import styles from "./login.module.scss";
import { ReactNode } from "react";

const Login = () => {
  return (
    <>
      <h1>밤편지</h1>
      <div>당신의 하루를 기록해보세요</div>
      <a href={`${process.env.NEXT_PUBLIC_API_URL}/auth/oauth2/kakao`}>
        <Image
          width={200}
          height={50}
          alt='카카오 로그인 버튼'
          src='/images/kakao-login-large-narrow.webp'
        />
      </a>
    </>
  );
};

export default Login;
