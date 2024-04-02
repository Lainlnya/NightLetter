import Redirect from "./Redirect";

import { QueryClient, dehydrate, HydrationBoundary } from '@tanstack/react-query';
import getInitialCards from '@/libs/getInitialCards';
import { Suspense } from "react";

import Loading from '@/app/loading';

export default async function page() {
    const queryClient = new QueryClient();
    await queryClient.prefetchQuery({
      queryKey: ['card', 'cards'],
      queryFn: getInitialCards,
    });

    const dehydrateState = dehydrate(queryClient);
  return (
    <HydrationBoundary state={dehydrateState}>
        <Suspense   fallback={<Loading loadingMessage="불러오는 중 입니다." />}>
        <Redirect/>
        </Suspense>
    </HydrationBoundary>
  )
}
