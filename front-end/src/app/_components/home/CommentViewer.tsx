import React from 'react'
import styles from "@/app/_components/home/commentViewer.module.scss";
import { NullableTarotDiary } from "@/types/card";

interface CommentViewerProps {
  data: NullableTarotDiary[];
  cardIndex: number;
}

export default function CommentViewer({data, cardIndex}:CommentViewerProps) {
  return (
    <div className={styles.content_container}>
        <div className={styles.content}>
        <h2>이날의 감정은요...</h2>
          <p>
            {data?.[cardIndex]?.content ? data?.[cardIndex]?.content : "이 날은 감정을 기록하지 않았어요."}
            </p>
        </div>
        {data?.[cardIndex]?.gptComment && 
        <div className={styles.comment_container}>
        <h3>당신의 사연을 보고 AI가 응원을 보냈어요.</h3>
            <p>
            &quot;{data?.[cardIndex]?.gptComment ? data?.[cardIndex]?.gptComment : "AI가 메시지를 보내지 않았어요."}&quot;
            </p>
        </div>
        }
    </div>
  )
}
