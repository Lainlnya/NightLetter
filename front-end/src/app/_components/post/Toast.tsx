import { useEffect } from 'react';
import styles from './toast.module.scss';

interface ToastProps {
  text: string;
  setToast: React.Dispatch<React.SetStateAction<boolean>>;
}

function Toast({ setToast, text }: ToastProps) {
  useEffect(() => {
    const timer = setTimeout(() => {
      setToast(false);
    }, 2000);
    return () => {
      clearTimeout(timer);
    };
  }, [setToast]);

  return (
    <div className={styles.toast}>
      <p>{text}</p>
    </div>
  );
}

export default Toast;
