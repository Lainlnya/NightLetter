import styles from './page.module.scss';
import MainPage from './_components/home/MainPage';
import { headers } from 'next/headers';

export default function Home() {
  return (
    <main className={styles.main}>
      <MainPage />
    </main>
  );
}
