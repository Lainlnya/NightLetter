"use client";

import styles from "./comment.module.scss";
import Image from "next/image";
import { TODAY } from "@/utils/dateFormat";
import { useQuery } from "@tanstack/react-query";
import { getGPTData } from "@/_apis/DiaryApis";
import Loading from "@/app/loading";
import { Messages } from "@/utils/msg";

const Comment: React.FC = () => {
  const { isLoading, data: gptComment } = useQuery({
    queryKey: ["GPTComments"],
    queryFn: () => getGPTData(),
  });
  console.log(gptComment);

  if (isLoading) {
    return <Loading loadingMessage={Messages.MAKING_COMMENT} />;
  }

  return (
    <>
      {gptComment && (
        <main className={styles.commentMain}>
          <h1>
            <div>{TODAY}</div>
            <div>하루의 코멘트</div>
          </h1>
          <section className={styles.cardSec}>
            <section>
              <div>과거</div>
              <div>현재</div>
              <div>미래</div>
            </section>
            <section>
              <Image
                className={styles.past}
                src={gptComment.past_url}
                width={110}
                height={190}
                alt="과거 카드"
              />
              <Image
                className={styles.present}
                alt="현재 카드"
                src={gptComment.now_url}
                width={110}
                height={190}
              />
              <Image
                width={110}
                height={190}
                alt="미래 카드"
                className={styles.future}
                src={gptComment.past_url}
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
