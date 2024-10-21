import React from 'react';
import { RouterProvider } from 'react-router';
import { createBrowserRouter } from 'react-router-dom';
import MainLayout from './MainLayout';

const RootPage = React.lazy(() => import('./pages/root'));
const NotFoundPage = React.lazy(() => import('./pages/not-found'));
const LoginPage = React.lazy(() => import('./pages/common/LoginPage'));
const RegistrationPage = React.lazy(
  () => import('./pages/common/RegistrationPage')
);

const router = createBrowserRouter([
  {
    path: '/',
    element: <MainLayout />,
    children: [
      { path: '/', element: <RootPage /> },
      { path: '*', element: <NotFoundPage /> },
      { path: '/logowanie', element: <LoginPage /> },
      { path: '/rejestracja', element: <RegistrationPage /> },
    ],
  },
]);

const AppRouter = () => {
  return <RouterProvider router={router} />;
};

export default AppRouter;
