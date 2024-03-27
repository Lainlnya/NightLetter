import React from "react";
import styles from "./loading.module.scss";
import { MoonLoader } from "react-spinners";

interface LoadingProps {
  loadingMessage: string;
}

const Loading: React.FC<LoadingProps> = ({ loadingMessage }) => {
  return (
    <main className={styles.main}>
      <MoonLoader
        className={styles.loader}
        color="#d5ccc1"
        size={80}
        speedMultiplier={0.5}
      />
      <div>{loadingMessage}</div>
    </main>
  );
};

export default Loading;
