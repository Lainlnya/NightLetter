'use client';

import React from 'react'
import Image from "next/image";
import styles from "./header.module.scss";
import alarm from "../../../../public/Icons/alarm_icon.svg";
import calender from "../../../../public/Icons/calender_icon.svg"
import useStore from '@/store/date'

export default function Header() {
    const { date } = useStore();

    return (
        <header className={styles.header}>
            <div className={styles.header_icons}>
                <Image src={alarm} alt="alarm" width={24} height={24} className={styles.header_icon} />
                <Image src={calender} alt="calender" width={24} height={24} className={styles.header_icon} />
            </div>
            <div className={styles.header_title}>
                <h1>{date}</h1>
            </div>
        </header>
    )
}
