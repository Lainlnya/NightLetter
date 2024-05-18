"use client";

import { usePathname } from "next/navigation";
import Link from "next/link";
import styles from "./GNB.module.scss";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faComment,
  faEnvelope,
  faHouse,
  faUser,
} from "@fortawesome/free-solid-svg-icons";
// import {}

const GNB = () => {
  const pathname = usePathname();

  return (
    <nav className={styles.nav}>
      <ul>
        <Link href='/'>
          <li className={`${pathname === "/" ? styles.active : ""}`}>
            <FontAwesomeIcon icon={faHouse} />
            <span>메인</span>
          </li>
        </Link>
        <Link href='/scrap'>
          <li className={`${pathname === "/scrap" ? styles.active : ""}`}>
            <FontAwesomeIcon icon={faEnvelope} />
            <span>스크랩</span>
          </li>
        </Link>
        <Link href='/chatting'>
          <li className={`${pathname === "/chatting" ? styles.active : ""}`}>
            <FontAwesomeIcon icon={faComment} />
            <span>채팅</span>
          </li>
        </Link>
        <Link href='/profile'>
          <li className={`${pathname === "/profile" ? styles.active : ""}`}>
            <FontAwesomeIcon icon={faUser} />
            <span>마이페이지</span>
          </li>
        </Link>
      </ul>
    </nav>
  );
};

export default GNB;
