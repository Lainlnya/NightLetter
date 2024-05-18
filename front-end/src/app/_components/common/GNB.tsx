'use client';

import { usePathname } from 'next/navigation';
import Link from 'next/link';
import styles from './GNB.module.scss';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBell, faComment, faEnvelope, faHouse, faUser } from '@fortawesome/free-solid-svg-icons';

const GNB = () => {
  const pathname = usePathname();

  return (
    <nav className={`${pathname.startsWith('/chatting/room') ? styles.none : styles.nav}`}>
      <ul>
        <Link href="/">
          <li className={`${pathname === '/' ? styles.active : ''}`}>
            <FontAwesomeIcon icon={faHouse} />
            <span>HOME</span>
          </li>
        </Link>
        <Link href="/scrap">
          <li className={`${pathname === '/scrap' ? styles.active : ''}`}>
            <FontAwesomeIcon icon={faEnvelope} />
            <span>스크랩</span>
          </li>
        </Link>
        <Link href="/notification">
          <li className={`${pathname === '/notification' ? styles.active : ''}`}>
            <FontAwesomeIcon icon={faBell} />
            <span>알림</span>
          </li>
        </Link>
        <Link href="/chatting">
          <li className={`${pathname === '/chatting' ? styles.active : ''}`}>
            <FontAwesomeIcon icon={faComment} />
            <span>CHAT</span>
          </li>
        </Link>
        <Link href="/profile">
          <li className={`${pathname === '/profile' ? styles.active : ''}`}>
            <FontAwesomeIcon icon={faUser} />
            <span>PROFILE</span>
          </li>
        </Link>
      </ul>
    </nav>
  );
};

export default GNB;
