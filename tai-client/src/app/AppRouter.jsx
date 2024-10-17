import React from 'react';
import { RouterProvider } from 'react-router';
import { createBrowserRouter } from 'react-router-dom';

const RootPage = React.lazy(() => import('./pages/root'));
const NotFoundPage = React.lazy(() => import('./pages/not-found'));

const router = createBrowserRouter([
  { path: '/', element: <RootPage /> },
  { path: '*', element: <NotFoundPage /> },
]);

const AppRouter = () => {
  return <RouterProvider router={router} />;
};

export default AppRouter;
