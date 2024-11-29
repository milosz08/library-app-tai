import React from 'react';
import { ThemeProvider } from '@mui/material/styles';
import { createRoot } from 'react-dom/client';
import AppRouter from './app/AppRouter';
import { AlertProvider } from './context/AlertContext';
import { AuthProvider } from './context/AuthContext';
import theme from './utils/theme';

createRoot(document.getElementById('app-mount')).render(
  <ThemeProvider theme={theme}>
    <React.StrictMode>
      <AuthProvider>
        <AlertProvider>
          <AppRouter />
        </AlertProvider>
      </AuthProvider>
    </React.StrictMode>
  </ThemeProvider>
);
