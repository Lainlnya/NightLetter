"use client";

import styles from "./comment.module.scss";
import Image from "next/image";
import { TODAY } from "@/utils/dateFormat";
import { useQuery } from "@tanstack/react-query";
import { getGPTData } from "@/_apis/DiaryApis";
import Loading from "@/app/loading";
import { Messages } from "@/utils/msg";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import closeIcon from "../../../../public/Icons/xmark-solid.svg";
import letterIcon from "../../../../public/Icons/envelope-regular.svg";
import tarot_background from "../../../../public/images/tarot-background.webp";

const Comment: React.FC = () => {
  const router = useRouter();
  const [showMessage, setShowMessage] = useState<boolean>(false);

  const { isLoading, data: gptComment } = useQuery({
    queryKey: ["GPTComments"],
    queryFn: () => getGPTData(),
  });

  useEffect(() => {
    const showMessageTimer = setTimeout(() => {
      setShowMessage(true);
    }, 5000);

    const clearTimer = setTimeout(() => {
      setShowMessage(false);
    }, 15000);
    return () => {
      clearTimeout(showMessageTimer);
      clearTimeout(clearTimer);
    };
  }, []);

  if (isLoading) {
    return <Loading loadingMessage={Messages.MAKING_COMMENT} />;
  }

  return (
    <>
      {gptComment && (
        <main className={styles.commentMain}>
          <section className={styles.titleSec}>
            <Image
              src={closeIcon}
              alt="뒤로가기"
              width={30}
              height={30}
              onClick={() => router.replace("/")}
            />
            <h1>
              <div>{TODAY}</div>
              <div>하루의 코멘트</div>
            </h1>
            <Image
              className={styles.recommend}
              src={letterIcon}
              alt="사연"
              width={30}
              height={30}
              onClick={() => router.push("/stories")}
            />
            {showMessage && (
              <section className={styles.blinking}>사연이 도착했어요!</section>
            )}
          </section>
          <section className={styles.cardSec}>
            <section>
              <div>과거</div>
              <div>현재</div>
              <div>미래</div>
            </section>
            <section>
              <Image
                className={styles.past}
                src={gptComment?.past_url || tarot_background}
                width={110}
                height={190}
                alt="과거 카드"
              />
              <Image
                className={styles.present}
                alt="현재 카드"
                src={gptComment?.now_url || tarot_background}
                width={110}
                height={190}
              />
              <Image
                width={110}
                height={190}
                alt="미래 카드"
                className={styles.future}
                src={gptComment?.future_url || tarot_background}
              />
            </section>
          </section>
          <section className={styles.commentSec}>
            {gptComment?.gptComment || "현재 코멘트가 지원되지 않습니다."}
          </section>
        </main>
      )}
    </>
  );
};

export default Comment;
