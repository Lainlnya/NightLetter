"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { useQuery } from "@tanstack/react-query";
import { getDateDiff } from "@/utils/dateFormat";
import getInitialCards from "@/libs/getInitialCards";
import getPastCardInfo from "@/libs/getPastCardInfo";

interface PastCardInfo {
  name: String,
  imgUrl: String,
  type: String,
  dir: null,
  keyword: String,
  desc: String
}



export default function Redirect(props: { pastCardInfo: PastCardInfo }) {
  const router = useRouter();
  const { data } = useQuery({ queryKey: ["card", "cards"], queryFn: getInitialCards });

  const date1 = new Date(data?.diaries?.[data.diaries.length - 1]?.date)
  const lastPastCard = props.pastCardInfo;

  const dateDiff = getDateDiff(date1, new Date());

  useEffect(() => {
    if (!data?.diaries?.length) {

      if (lastPastCard === null) router.push("/tarot?info=past");
      if (lastPastCard !== null) router.push("/");
    }

    if (dateDiff >= 28) {
      if (lastPastCard === null) router.push("/tarot?info=past");
      if (lastPastCard !== null) router.push("/");
    }

    if (dateDiff < 28) {
      router.push("/");
    }

  }, []);

  return null;
}