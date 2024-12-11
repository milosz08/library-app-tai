import React from 'react';
import { RouterProvider } from 'react-router';
import { createBrowserRouter } from 'react-router-dom';
import GuestRoute from './GuestRoute';
import MainLayout from './MainLayout';
import ProtectedRoute from './ProtectedRoute';

const RootPage = React.lazy(() => import('../pages/common/HomePage'));
const NotFoundPage = React.lazy(() => import('../pages/common/NotFoundPage'));
const LoginPage = React.lazy(() => import('../pages/auth/LoginPage'));
const RequestResetPassoword = React.lazy(
  () => import('../pages/common/RequestResetPassoword')
);
const RegistrationPage = React.lazy(
  () => import('../pages/auth/RegistrationPage')
);
const ActivationPage = React.lazy(() => import('../pages/auth/ActivationPage'));
const LogsPage = React.lazy(() => import('../pages/admin/LogsPage'));
const EmployerCreationPage = React.lazy(
  () => import('../pages/admin/EmployerCreationPage')
);
const EmployersPage = React.lazy(() => import('../pages/admin/EmployersPage'));
const EditUserDetailsPage = React.lazy(
  () => import('../pages/common/EditUserDetailsPage')
);
const RenewResetPassoword = React.lazy(
  () => import('../pages/common/RenewResetPassword')
);
const FirstAccessPage = React.lazy(
  () => import('../pages/employer/FirstAccessPage')
);
const AddBookPage = React.lazy(() => import('../pages/employer/AddBookPage'));
const EmployerBooksPage = React.lazy(
  () => import('../pages/employer/EmployerBooksPage')
);
const CustomerBooksPage = React.lazy(
  () => import('../pages/customer/CustomerBooksPage')
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
        path: '/admin/zdarzenia',
        element: (
          <ProtectedRoute allowedRoles={['ADMIN']}>
            <LogsPage />
          </ProtectedRoute>
        ),
      },
      {
        path: '/admin/dodaj-pracownika',
        element: (
          <ProtectedRoute allowedRoles={['ADMIN']}>
            <EmployerCreationPage />
          </ProtectedRoute>
        ),
      },
      {
        path: '/admin/pracownicy',
        element: (
          <ProtectedRoute allowedRoles={['ADMIN']}>
            <EmployersPage />
          </ProtectedRoute>
        ),
      },
      {
        path: '/pracownik/dodaj-ksiazke',
        element: (
          <ProtectedRoute allowedRoles={['EMPLOYER']}>
            <AddBookPage />
          </ProtectedRoute>
        ),
      },
      {
        path: '/pracownik/ksiazki',
        element: (
          <ProtectedRoute allowedRoles={['EMPLOYER']}>
            <EmployerBooksPage />
          </ProtectedRoute>
        ),
      },
      {
        path: '/ksiazki',
        element: (
          <ProtectedRoute allowedRoles={['CUSTOMER']}>
            <CustomerBooksPage />
          </ProtectedRoute>
        ),
      },
      {
        path: '/pracownik/pierwszy-dostep/:token',
        element: <FirstAccessPage />,
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
