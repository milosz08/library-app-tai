import React from 'react';
import { ThemeProvider } from '@mui/material/styles';
import { createRoot } from 'react-dom/client';
import AppRouter from './app/AppRouter';
import theme from './utils/theme';

createRoot(document.getElementById('app-mount')).render(
  <ThemeProvider theme={theme}>
    <React.StrictMode>
      <AppRouter />
    </React.StrictMode>
  </ThemeProvider>
);
