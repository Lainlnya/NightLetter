import styles from "./login.module.scss";

export default function LoginLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return <main className={styles.main}>{children}</main>;
}
