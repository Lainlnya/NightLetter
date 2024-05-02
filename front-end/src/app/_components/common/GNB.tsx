import Link from "next/link";
import home from "../../../../public/icons/house-solid.svg";
import envelope from "../../../../public/icons/envelope-solid.svg";
import chat from "../../../../public/icons/comment-solid.svg";
import profile from "../../../../public/icons/user-solid.svg";
import styles from "./GNB.module.scss";
import Image from "next/image";

const GNB = () => {
  return (
    <nav className={styles.nav}>
      <ul>
        <Link href="/">
          <li>
            <Image src={home} alt="home icon" width={29} height={26} />
            <span>HOME</span>
          </li>
        </Link>
        <Link href="/scrap">
          <li>
            <Image src={envelope} alt="home icon" width={29} height={26} />
            <span>스크랩</span>
          </li>
        </Link>
        <Link href="/chatting">
          <li>
            <Image src={chat} alt="home icon" width={29} height={26} />
            <span>CHAT</span>
          </li>
        </Link>
        <Link href="/profile">
          <li>
            <Image src={profile} alt="home icon" width={29} height={26} />
            <span>PROFILE</span>
          </li>
        </Link>
      </ul>
    </nav>
  );
};

export default GNB;
