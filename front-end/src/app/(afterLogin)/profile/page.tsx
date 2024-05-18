"use client";

import Image from "next/image";
import styles from "./profile.module.scss";
import profileImage from "../../../../public/images/profile.jpg";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faAngleRight, faPencil } from "@fortawesome/free-solid-svg-icons";
import { useState } from "react";
import UserPopUp from "@/app/_components/user/UserPopUp";
import { deleteCookie } from "../action";
import { useRouter } from "next/navigation";

const Profile: React.FC = () => {
  const [isPopupShown, setPopupShown] = useState<boolean>(false);
  const router = useRouter();

  return (
    <section className={styles.profile}>
      <h1>프로필</h1>
      <section>
        <Image
          className={styles.profileImage}
          src={profileImage}
          width={150}
          height={150}
          alt='프로필'
          priority
        />
        <input readOnly type='text' id='nickname' value='어이없는 어피치+' />
        <label htmlFor='nickname'>
          <FontAwesomeIcon icon={faPencil} />
        </label>
      </section>
      <section className={styles.buttonSec}>
        <button
          onClick={() => {
            // 성공했는지 확인할 것
            deleteCookie();
            router.replace("/login");
          }}
        >
          로그아웃
        </button>
        <button onClick={() => setPopupShown(true)} className={styles.signout}>
          <div></div>
          <span>회원탈퇴</span>
          <FontAwesomeIcon icon={faAngleRight} />
        </button>
      </section>
      {isPopupShown && <UserPopUp onOff={setPopupShown} />}
    </section>
  );
};

export default Profile;
