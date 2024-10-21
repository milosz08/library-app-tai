import { AppBar, Typography } from '@mui/material';

const Navbar = () => {
  return (
    <AppBar position="static" sx={{ bgcolor: 'custom.900' }}>
      <Typography variant="h6" component="div" sx={{ flexGrow: 1, padding: 2 }}>
        Projekt TAI - biblioteka
      </Typography>
    </AppBar>
  );
};

export default Navbar;
