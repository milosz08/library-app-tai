import React from 'react';
import { RouterProvider } from 'react-router';
import { createBrowserRouter } from 'react-router-dom';
import GuestRoute from './GuestRoute';
import MainLayout from './MainLayout';
import ProtectedRoute from './ProtectedRoute';

const RootPage = React.lazy(() => import('../pages/root/HomePage'));
const NotFoundPage = React.lazy(() => import('../pages/common/NotFoundPage'));
const LoginPage = React.lazy(() => import('../pages/auth/LoginPage'));
const RequestResetPassoword = React.lazy(
  () => import('../pages/common/RequestResetPassoword')
);
const RegistrationPage = React.lazy(
  () => import('../pages/auth/RegistrationPage')
);
const ActivationPage = React.lazy(() => import('../pages/auth/ActivationPage'));
const EditUserDetailsPage = React.lazy(
  () => import('../pages/common/EditUserDetailsPage')
);
const RenewResetPassoword = React.lazy(
  () => import('../pages/common/RenewResetPassword')
);

const router = createBrowserRouter([
  {
    path: '/',
    element: <MainLayout />,
    children: [
      {
        path: '/',
        element: (
          <ProtectedRoute allowedRoles={['CUSTOMER', 'ADMIN', 'EMPLOYER']}>
            <RootPage />
          </ProtectedRoute>
        ),
      },
      {
        path: '/edycja-danych',
        element: (
          <ProtectedRoute allowedRoles={['CUSTOMER', 'ADMIN']}>
            <EditUserDetailsPage />
          </ProtectedRoute>
        ),
      },
      {
        path: '/logowanie',
        element: (
          <GuestRoute>
            <LoginPage />
          </GuestRoute>
        ),
      },
      {
        path: '/rejestracja',
        element: (
          <GuestRoute>
            <RegistrationPage />
          </GuestRoute>
        ),
      },
      {
        path: '/aktywacja/:token',
        element: (
          <GuestRoute>
            <ActivationPage />
          </GuestRoute>
        ),
      },
      {
        path: '/przypomnij-haslo',
        element: (
          <GuestRoute>
            <RequestResetPassoword />
          </GuestRoute>
        ),
      },
      {
        path: '/przypomnij-haslo/:token',
        element: (
          <GuestRoute>
            <RenewResetPassoword />
          </GuestRoute>
        ),
      },
      { path: '*', element: <NotFoundPage /> },
    ],
  },
]);

const AppRouter = () => {
  return <RouterProvider router={router} />;
};

export default AppRouter;
