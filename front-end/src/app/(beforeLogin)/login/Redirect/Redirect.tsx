"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { useQuery } from "@tanstack/react-query";
import { getDateDiff } from "@/utils/dateFormat";
import getInitialCards from "@/libs/getInitialCards";


export default function Redirect() {
  const router = useRouter();
  const { data } = useQuery({ queryKey: ["card", "cards"], queryFn: getInitialCards });

  const date1 = new Date(data?.diaries?.[data.diaries.length - 1]?.date)
  const lastPastCard = data?.diaries?.[data.diaries.length - 1]?.nowCard;

  const dateDiff = getDateDiff(date1, new Date());

  useEffect(() => {
    // 마지막으로 뽑은지 4주 이상 지났거나, 오늘 뽑은 카드가 없을 경우 타로페이지로 이동
    if (dateDiff > 28 || lastPastCard === null) router.push('/tarot');
    else router.push('/');
  }, []);
  return null;
}