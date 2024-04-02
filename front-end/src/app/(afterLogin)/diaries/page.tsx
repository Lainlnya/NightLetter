import MainPage from '@/app/_components/diaries/MainPage'
import { QueryClient, dehydrate, HydrationBoundary } from '@tanstack/react-query';
import getInitialDiaries from "@/libs/getInitialDiaries"

export default async function Diaries() {
    const queryClient = new QueryClient();
    await queryClient.prefetchQuery({
        queryKey: ['diary', 'diaries'],
        queryFn: () => getInitialDiaries(),

    });
    const dehydrateState = dehydrate(queryClient);

    return (
        <>
            <HydrationBoundary state={dehydrateState}>
                <MainPage />
            </HydrationBoundary>
        </>
    )
}
