import styles from "./page.module.scss";
import MainPage from "./_components/home/MainPage";

export default function Home() {
  return (
    <main className={styles.main}>
      <MainPage />
    </main>
  );
}
