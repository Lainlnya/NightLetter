import { ChatMessageResponse } from "@/types/chat";
import Image from "next/image";
import styles from "./chatBlock.module.scss";
import { convertTime } from "@/utils/dateFormat";

export const MyChat: React.FC<{ msg: ChatMessageResponse; last: boolean }> = ({
  msg,
  last,
}) => {
  const { sendTime, message } = msg;
  return (
    <section className={`${styles.message} ${styles.me}`}>
      {last && <span>{convertTime(sendTime)}</span>}
      <span className={`${styles.msgBox} ${styles.noMargin}`}>{message}</span>
    </section>
  );
};

// 다른 사람이 보낸 채팅
export const OthersChat: React.FC<{
  msg: ChatMessageResponse;
  last: boolean;
}> = ({ msg, last }) => {
  const { message, sendTime } = msg;
  return (
    <section className={styles.message}>
      <span className={styles.msgBox}>{message}</span>
      {last && <span>{convertTime(sendTime)}</span>}
    </section>
  );
};

export const OthersChatWithThumbnail: React.FC<{
  msg: ChatMessageResponse;
  last: boolean;
}> = ({ msg, last }) => {
  const { nickname, message, sendTime } = msg;
  return (
    <section className={styles.oneConv}>
      <Image
        className={styles.profileImg}
        src={
          "https://ssafy-tarot-01.s3.ap-northeast-2.amazonaws.com/profile/1.jpg"
        }
        width={40}
        height={40}
        alt='profile'
      />
      <div className={styles.boxWithImage}>
        <div>{nickname}</div>
        <div className={styles.box}>
          <span className={styles.msgBox}>{message}</span>
          {last && <span>{convertTime(sendTime)}</span>}
        </div>
      </div>
    </section>
  );
};
