"use client";

import {useEffect} from "react";
import {useRouter} from "next/navigation";
import {useQuery} from "@tanstack/react-query";
import getInitialCards from "@/libs/getInitialCards";


export default function Redirect() {
  const router = useRouter();
  const { data } = useQuery({ queryKey: ["card", "cards"], queryFn: getInitialCards });

  const date1 = new Date(data?.diaries?.[data.diaries.length - 1]?.date)
  const lastPastCard = data?.diaries?.[data.diaries.length - 1]?.nowCard;
  
  const diffTime = Math.abs(date1 - new Date());
  
  // 밀리초를 일 단위로 변환
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)); 
  
  console.log(diffDays);

  useEffect(() => {
    if(diffDays > 28 || lastPastCard === null) router.push('/tarot');
    else router.push('/');
  }, []);
  return null;
}