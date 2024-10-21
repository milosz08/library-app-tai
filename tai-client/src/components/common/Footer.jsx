import { Box, Typography } from '@mui/material';

const Footer = () => {
  return (
    <Box
      component="footer"
      sx={{
        padding: '20px',
        backgroundColor: 'custom.900',
        color: 'white',
        textAlign: 'center',
      }}>
      <Typography variant="body2">© Projekt TAI - biblioteka</Typography>
    </Box>
  );
};

export default Footer;
