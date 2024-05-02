import GNB from "../_components/common/GNB";
import styles from "./layout.module.scss";

export default function AfterLoginLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <main className={styles.main}>
      {children}
      <GNB />
    </main>
  );
}
