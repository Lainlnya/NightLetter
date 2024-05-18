import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import styles from './notification.module.scss';
import { motion } from 'framer-motion';
import { faEnvelope } from '@fortawesome/free-solid-svg-icons';
import { useEffect, useState } from 'react';

export default function Notification({ notification }: { notification: string }) {
  const [y, setY] = useState(-10);

  useEffect(() => {
    if (notification !== '') {
      setY(0);
      const timer = setTimeout(() => {
        setY(-10);
      }, 2000);
      return () => clearTimeout(timer);
    }
  }, [notification]);

  return (
    <motion.div animate={{ y }} transition={{ type: 'spring', damping: 10, stiffness: 100 }}>
      <div className={`${notification === '' ? styles.none : styles.noti}`}>
        <div>
          <FontAwesomeIcon icon={faEnvelope} />
        </div>
        <span>{notification}</span>
      </div>
    </motion.div>
  );
}
