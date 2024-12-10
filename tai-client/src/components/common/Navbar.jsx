import { AppBar, Box, Toolbar, Typography } from '@mui/material';
import { useAuth } from '../../hooks/useAuth';
import NavigationButton from './NavigationButton';
import UserProfile from './UserProfile';

const Navbar = () => {
  const { isAuthenticated, role } = useAuth();

  return (
    <AppBar position="static" sx={{ bgcolor: 'custom.900' }}>
      <Toolbar>
        <Box sx={{ display: 'flex', alignItems: 'center', flexGrow: 1 }}>
          <Typography variant="h6" component="div" sx={{ marginRight: 2 }}>
            Projekt TAI - biblioteka
          </Typography>
          <NavigationButton label="Home" path="/" />
          {isAuthenticated && (
            <>
              {role === 'ADMIN' && (
                <>
                  <NavigationButton label="Zdarzenia" path="/admin/zdarzenia" />
                  <NavigationButton
                    label="Dodaj pracownika"
                    path="/admin/dodaj-pracownika"
                  />
                  <NavigationButton
                    label="Pracownicy"
                    path="/admin/pracownicy"
                  />
                </>
              )}
            </>
          )}
        </Box>

        {isAuthenticated && <UserProfile />}
      </Toolbar>
    </AppBar>
  );
};

export default Navbar;
