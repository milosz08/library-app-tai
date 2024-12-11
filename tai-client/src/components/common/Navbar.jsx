import { useState } from 'react';
import MenuIcon from '@mui/icons-material/Menu';
import {
  AppBar,
  Box,
  IconButton,
  Menu,
  MenuItem,
  Toolbar,
  Typography,
  useMediaQuery,
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '~/hooks/useAuth';
import NavigationButton from './NavigationButton';
import UserProfile from './UserProfile';

const Navbar = () => {
  const { isAuthenticated, role } = useAuth();
  const [menuAnchor, setMenuAnchor] = useState(null);
  const isMobile = useMediaQuery(theme => theme.breakpoints.down('sm'));
  const navigate = useNavigate();

  const handleMenuOpen = event => {
    setMenuAnchor(event.currentTarget);
  };

  const handleMenuClose = () => {
    setMenuAnchor(null);
  };

  const menuItems = [
    { label: 'Home', path: '/' },
    ...(isAuthenticated && role === 'ADMIN'
      ? [
          { label: 'Zdarzenia', path: '/admin/zdarzenia' },
          { label: 'Dodaj pracownika', path: '/admin/dodaj-pracownika' },
          { label: 'Pracownicy', path: '/admin/pracownicy' },
        ]
      : []),
    ...(isAuthenticated && role === 'EMPLOYER'
      ? [
          { label: 'Dodaj książkę', path: '/pracownik/dodaj-ksiazke' },
          { label: 'Książki', path: '/pracownik/ksiazki' },
        ]
      : []),
    ...(isAuthenticated && role === 'CUSTOMER'
      ? [
          { label: 'Książki', path: '/ksiazki' },
          { label: 'Wypożyczone książki', path: '/wypozyczone-ksiazki' },
        ]
      : []),
  ];

  return (
    <AppBar position="static" sx={{ bgcolor: 'custom.900' }}>
      <Toolbar>
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          Projekt TAI - biblioteka
        </Typography>

        {isMobile ? (
          <>
            <IconButton
              color="inherit"
              edge="start"
              onClick={handleMenuOpen}
              sx={{ mr: 2 }}>
              <MenuIcon />
            </IconButton>
            <Menu
              anchorEl={menuAnchor}
              open={Boolean(menuAnchor)}
              onClose={handleMenuClose}
              slotProps={{
                sx: {
                  bgcolor: 'custom.900',
                  color: 'white',
                },
              }}>
              {menuItems.map(item => (
                <MenuItem
                  key={item.label}
                  onClick={() => {
                    navigate(item.path);
                    handleMenuClose();
                  }}
                  sx={{
                    '&:hover': {
                      bgcolor: 'custom.800',
                    },
                  }}>
                  {item.label}
                </MenuItem>
              ))}
            </Menu>
          </>
        ) : (
          <Box sx={{ display: 'flex', alignItems: 'center' }}>
            {menuItems.map(item => (
              <NavigationButton
                key={item.label}
                label={item.label}
                path={item.path}
              />
            ))}
          </Box>
        )}

        {isAuthenticated && <UserProfile />}
      </Toolbar>
    </AppBar>
  );
};

export default Navbar;
