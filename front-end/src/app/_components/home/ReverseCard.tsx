"use client";

import Image from "next/image";
import styles from "./cardSlider.module.scss";
import tarot_background from "../../../../public/images/tarot-background.png";
import { parseDateToKoreanFormatWithDay } from "@/utils/dateFormat";
import { motion, useMotionValue } from "framer-motion";
import React, { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { DRAG_BUFFER } from "@/utils/animation";
import getInitialCards from "@/libs/getInitialCards";
import { useQuery } from "@tanstack/react-query";
import useStore from "@/store/date";
import {CalendarProps} from "@/types/calender";
import {DiaryEntry} from "@/types/card";
import { TODAY } from "@/utils/dateFormat";