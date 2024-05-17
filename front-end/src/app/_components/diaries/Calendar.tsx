"use client";

import { useState } from "react";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import "./calendar.scss";
import dayjs from "dayjs";
import store from "@/store/date";

export default function CalendarComponent() {
  const [value, setValue] = useState(new Date());

  const { PIVOT_DATE_YYYY_MM_DD, setPivotDate } = store();

  function handleChange(nextValue:any) {
    setValue(nextValue);
    
    setPivotDate(dayjs(nextValue).format("YYYY-MM-DD"));
  }

  return (
    <div className="calendar">
      <Calendar
        onChange={handleChange}
        value={value}
        prev2Label={null}
        next2Label={null} 
        showNeighboringMonth={false}
        formatDay={(locale, date) => dayjs(date).format("DD")}
      />
    </div>
  );
}