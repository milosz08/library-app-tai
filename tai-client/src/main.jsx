import React from 'react';
import { ThemeProvider } from '@mui/material/styles';
import { createRoot } from 'react-dom/client';
import AppRouter from './app/AppRouter';
import { AlertProvider } from './context/AlertContext';
import { AuthProvider } from './context/AuthContext';
import { LoaderProvider } from './context/LoaderContenxt';
import theme from './utils/theme';

createRoot(document.getElementById('app-mount')).render(
  <ThemeProvider theme={theme}>
    <React.StrictMode>
      <LoaderProvider>
        <AuthProvider>
          <AlertProvider>
            <AppRouter />
          </AlertProvider>
        </AuthProvider>
      </LoaderProvider>
    </React.StrictMode>
  </ThemeProvider>
);
