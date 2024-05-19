"use server";

import { cookies } from "next/headers";

export async function deleteCookie() {
  cookies().set("access-token", "", { maxAge: 0 });
}
