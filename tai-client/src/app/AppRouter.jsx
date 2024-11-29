import React from 'react';
import PropTypes from 'prop-types';
import { RouterProvider } from 'react-router';
import { Navigate, createBrowserRouter } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import MainLayout from './MainLayout';

const RootPage = React.lazy(() => import('./pages/root/HomePage'));
const NotFoundPage = React.lazy(() => import('./pages/common/NotFoundPage'));
const LoginPage = React.lazy(() => import('./pages/auth/LoginPage'));
const RegistrationPage = React.lazy(
  () => import('./pages/auth/RegistrationPage')
);
const ActivationPage = React.lazy(() => import('./pages/auth/ActivationPage'));

const ProtectedRoute = ({ children }) => {
  const { isAuthenticated } = useAuth();
  return isAuthenticated ? children : <Navigate to="/logowanie" />;
};

ProtectedRoute.propTypes = {
  children: PropTypes.node.isRequired,
};

const GuestRoute = ({ children }) => {
  const { isAuthenticated } = useAuth();
  return !isAuthenticated ? children : <Navigate to="/" />;
};

GuestRoute.propTypes = {
  children: PropTypes.node.isRequired,
};

const router = createBrowserRouter([
  {
    path: '/',
    element: <MainLayout />,
    children: [
      {
        path: '/',
        element: (
          <ProtectedRoute>
            <RootPage />
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
      { path: '*', element: <NotFoundPage /> },
    ],
  },
]);

const AppRouter = () => {
  return <RouterProvider router={router} />;
};

export default AppRouter;
