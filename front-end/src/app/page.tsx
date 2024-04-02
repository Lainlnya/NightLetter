import styles from './page.module.scss';
import MainPage from './_components/home/MainPage';
import { QueryClient, dehydrate, HydrationBoundary } from '@tanstack/react-query';
import getInitialCards from '@/libs/getInitialCards';

export default async function Home() {
  const queryClient = new QueryClient();
  await queryClient.prefetchQuery({
    queryKey: ['card', 'cards'],
    queryFn: getInitialCards,
  });
  const dehydrateState = dehydrate(queryClient);
  return (
    <main className={styles.main}>
      <HydrationBoundary state={dehydrateState}>
        <MainPage />
      </HydrationBoundary>
    </main>
  )
}
