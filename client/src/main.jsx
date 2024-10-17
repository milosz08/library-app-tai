import React from 'react';
import { createRoot } from 'react-dom/client';
import AppRouter from './app/AppRouter';

createRoot(document.getElementById('app-mount')).render(
  <React.StrictMode>
    <AppRouter />
  </React.StrictMode>
);
