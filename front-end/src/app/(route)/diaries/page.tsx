import React from 'react'
import styles from './diaries.module.scss'
import diary from '../../../../public/images/diary_background.svg'
import Image from 'next/image'
import back_button from '../../../../public/Icons/back_button.svg'
import alarm from '../../../../public/Icons/calender_icon.svg'

export default function page() {
    return (
        <div className={styles.root}>
            <header className={styles.header}>
                <Image src={back_button} alt="back_button" className={styles.back_button} />
                <h1>2024년 3월 11일</h1>
                <Image src={alarm} alt="alarm" />
            </header>
            <main className={styles.wrapper_card}>
                <Image src={diary} alt="diary" />
            </main>
            <footer className={styles.footer}>
                <p>카드를 좌우로 슬라이드하여 날짜를 이동할 수 있어요.</p>
            </footer>
        </div>
    )
}
