import { useEffect, useState } from 'react';
import AccountCircle from '@mui/icons-material/AccountCircle';
import {
  Alert,
  AppBar,
  IconButton,
  Menu,
  MenuItem,
  Toolbar,
  Typography,
} from '@mui/material';
import { useNavigate } from 'react-router';
import { details } from '../../api/userApi';
import { useAuth } from '../../hooks/useAuth';
import { useLoader } from '../../hooks/useLoader';

const Navbar = () => {
  const { isAuthenticated, handleLogout, roleName } = useAuth();
  const { setIsLoading } = useLoader();
  const [user, setUser] = useState({
    firstName: 'Gość',
    lastName: '',
    email: '',
  });
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const [anchorEl, setAnchorEl] = useState(null);

  const handleMenuOpen = event => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleEditProfile = () => {
    navigate('/edycja/danych');
    handleMenuClose();
  };

  const handleLogoutClick = () => {
    handleLogout();
    handleMenuClose();
  };

  useEffect(() => {
    const fetchUserDetails = async () => {
      setIsLoading(true);
      try {
        const userData = await details();
        setUser(userData);
        setError(null);
      } catch (err) {
        setError('Wystąpił błąd serwera.');
      } finally {
        setIsLoading(false);
      }
    };

    if (isAuthenticated) {
      fetchUserDetails();
    }
  }, [isAuthenticated, setIsLoading]);

  return (
    <AppBar position="static" sx={{ bgcolor: 'custom.900' }}>
      <Toolbar>
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          Projekt TAI - biblioteka
        </Typography>
        {isAuthenticated && (
          <>
            {error ? (
              <Alert
                severity="error"
                sx={{
                  backgroundColor: 'transparent',
                  color: 'inherit',
                }}>
                {error}
              </Alert>
            ) : (
              <>
                <IconButton
                  size="large"
                  edge="end"
                  color="inherit"
                  onClick={handleMenuOpen}>
                  <AccountCircle />
                </IconButton>
                <Menu
                  anchorEl={anchorEl}
                  open={Boolean(anchorEl)}
                  onClose={handleMenuClose}
                  slotProps={{
                    paper: {
                      sx: {
                        bgcolor: 'custom.800',
                        color: 'custom.50',
                        '& .MuiMenuItem-root:hover': {
                          bgcolor: 'custom.700',
                        },
                      },
                    },
                  }}>
                  <MenuItem
                    disabled
                    sx={{
                      justifyContent: 'center',
                      textAlign: 'center',
                      flexDirection: 'column',
                      color: 'custom.300',
                    }}>
                    <Typography variant="body1" fontWeight="bold">
                      {user.firstName} {user.lastName}
                    </Typography>
                    <Typography variant="body2">{user.email}</Typography>
                  </MenuItem>
                  <MenuItem
                    disabled
                    sx={{
                      justifyContent: 'center',
                      textAlign: 'center',
                      color: 'customBlue.100',
                    }}>
                    <Typography variant="body2">Rola: {roleName}</Typography>
                  </MenuItem>
                  <MenuItem
                    onClick={handleEditProfile}
                    sx={{ color: 'custom.50', textAlign: 'center' }}>
                    Edytuj dane
                  </MenuItem>
                  <MenuItem
                    onClick={handleLogoutClick}
                    sx={{ color: 'custom.50', textAlign: 'center' }}>
                    Wyloguj się
                  </MenuItem>
                </Menu>
              </>
            )}
          </>
        )}
      </Toolbar>
    </AppBar>
  );
};

export default Navbar;
