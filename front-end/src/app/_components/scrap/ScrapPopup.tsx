import { ScrapItem } from "@/types/apis";
import styles from "./scrapPopup.module.scss";
import React, { useEffect, useRef } from "react";
import Image from "next/image";
import { formattedDate } from "@/utils/dateFormat";

const ScrapPopup: React.FC<{
  scrapInfo: ScrapItem;
  onClose: (open: boolean) => void;
}> = ({ scrapInfo, onClose }) => {
  const { nickname, content, imgUrl, scrappedAt } = scrapInfo;

  const popupRef = useRef<HTMLElement>(null);

  const handleClickOutside = (
    event: React.MouseEvent<HTMLElement> | React.TouchEvent<HTMLElement>
  ) => {
    if (popupRef.current && !popupRef.current.contains(event.target as Node)) {
      event.preventDefault();
      event.stopPropagation();
      onClose(false);
    }
  };

  useEffect(() => {
    const handleMouseDown = (event: React.MouseEvent<HTMLElement>) =>
      handleClickOutside(event as any);
    const handleTouchStart = (event: React.TouchEvent<HTMLElement>) =>
      handleClickOutside(event);

    document.addEventListener("mousedown", handleMouseDown as any, {
      passive: false,
    });
    document.addEventListener("touchstart", handleTouchStart as any, {
      passive: false,
    });

    return () => {
      document.removeEventListener("mousedown", handleMouseDown as any);
      document.removeEventListener("touchstart", handleTouchStart as any);
    };
  }, [popupRef, onClose]);

  return (
    <section className={styles.popup} onClick={handleClickOutside}>
      <section
        ref={popupRef}
        className={styles.card}
        onClick={(e) => {
          e.stopPropagation();
          e.preventDefault();
        }}
      >
        <Image
          className={styles.card}
          src={imgUrl}
          width={70}
          height={90}
          alt='뽑은 카드'
        />
        <div className={styles.contents}>
          <h1>{nickname}</h1>
          <span>{formattedDate(scrappedAt)}</span>
          <div>{content}</div>
        </div>
      </section>
    </section>
  );
};

export default ScrapPopup;
