import { Box, CircularProgress } from '@mui/material';

const SuspenseLoader = () => {
  return (
    <Box
      sx={{
        position: 'fixed',
        top: 0,
        left: 0,
        width: '100vw',
        height: '100vh',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        bgcolor: 'rgba(0, 0, 0, 0.5)',
        zIndex: 1300,
      }}>
      <CircularProgress
        sx={{
          color: 'customBlue.500',
        }}
        size={100}
        thickness={5}
      />
    </Box>
  );
};

export default SuspenseLoader;
