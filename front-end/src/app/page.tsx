import styles from './page.module.scss';
import MainPage from './_components/home/MainPage';
import { QueryClient, dehydrate, HydrationBoundary } from '@tanstack/react-query';
import getCardListByPeriod from '@/libs/getCardListByPeriod';
import GNB from './_components/common/GNB';
import Portal from './_components/modal/ModalPortal';
import { WebSocketProvider } from '@/context/WebSocketClient';

export default async function Home() {
  const queryClient = new QueryClient();
  await queryClient.prefetchQuery({
    queryKey: ['card', 'cards'],
    queryFn: () => getCardListByPeriod('2024-04-01', '2021-05-10'),
  });

  const dehydrateState = dehydrate(queryClient);
  return (
    <WebSocketProvider>
      <main className={styles.main}>
        <HydrationBoundary state={dehydrateState}>
          <MainPage />
        </HydrationBoundary>
      </main>
      <GNB />
    </WebSocketProvider>
  );
}
