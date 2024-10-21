// src/MainLayout.js
import { Suspense } from 'react';
import { Box } from '@mui/material';
import { Outlet } from 'react-router-dom';
import Footer from '../components/common/Footer';
import Navbar from '../components/common/Navbar';
import SuspenseLoader from '../components/common/SuspenseLoader';

const MainLayout = () => {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        minHeight: '100vh',
        backgroundColor: 'custom.800',
      }}>
      <Navbar />
      <Box
        sx={{
          flex: 1,
          paddingTop: '20px',
          paddingBottom: '20px',
        }}>
        <Suspense fallback={<SuspenseLoader />}>
          <Outlet />
        </Suspense>
      </Box>
      <Footer />
    </Box>
  );
};

export default MainLayout;
