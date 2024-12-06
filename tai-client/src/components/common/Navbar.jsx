import { AppBar, Button, Toolbar, Typography } from '@mui/material';
import { useAuth } from '../../hooks/useAuth';

const Navbar = () => {
  const { isAuthenticated, handleLogout } = useAuth();

  return (
    <AppBar position="static" sx={{ bgcolor: 'custom.900' }}>
      <Toolbar>
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          Projekt TAI - biblioteka
        </Typography>
        {isAuthenticated && (
          <Button color="inherit" onClick={handleLogout}>
            Wyloguj się
          </Button>
        )}
      </Toolbar>
    </AppBar>
  );
};

export default Navbar;
