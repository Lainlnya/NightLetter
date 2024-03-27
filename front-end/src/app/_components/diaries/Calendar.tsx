"use client";

import { useState } from "react";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import "./calendar.scss";
import moment from "moment";

type ValuePiece = Date | null;

type Value = ValuePiece | [ValuePiece, ValuePiece];

// 아무래도 전체를 다 가져오기는 어려울 듯 보입니다.
// 오늘 날짜 표시해주고, 해당 날짜 클릭했을 때 없으면 없다고 표시
// 있으면 아래 카드 이동하는걸로 해야할 것 같습니다.
export default function CalendarComponent() {
  const [value, onChange] = useState<Value>(new Date());
  return (
    <div className="calendar">
      <Calendar
        locale="en"
        onChange={onChange}
        value={value}
        // minDetail="month"
        // maxDetail="month"
        navigationLabel={null}
        showNeighboringMonth={false}
        formatDay={(locale, date) => moment(date).format("DD")}
      />
    </div>
  );
}
