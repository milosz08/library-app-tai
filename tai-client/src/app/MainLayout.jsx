import { Suspense } from 'react';
import { Outlet } from 'react-router-dom';
import SuspenseLoader from '../components/SuspenseLoader';

const MainLayout = () => {
  return (
    <Suspense fallback={<SuspenseLoader />}>
      <header>HEADER</header>
      <Outlet />
      <footer>FOOTER</footer>
    </Suspense>
  );
};

export default MainLayout;
