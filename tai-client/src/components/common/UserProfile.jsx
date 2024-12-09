import { useEffect, useState } from 'react';
import AccountCircle from '@mui/icons-material/AccountCircle';
import { IconButton, Menu, MenuItem, Typography } from '@mui/material';
import { useNavigate } from 'react-router';
import { deleteAccount, details } from '../../api/userApi';
import { useAlert } from '../../hooks/useAlert';
import { useAuth } from '../../hooks/useAuth';
import { useLoader } from '../../hooks/useLoader';
import ConfirmationModal from './ConfirmationModal';

const UserProfile = () => {
  const { isAuthenticated, handleLogout, roleName, role } = useAuth();
  const { setIsLoading } = useLoader();
  const { addAlert } = useAlert();
  const [user, setUser] = useState({
    firstName: 'Gość',
    lastName: '',
    email: '',
  });
  const [anchorEl, setAnchorEl] = useState(null);
  const [isModalOpen, setModalOpen] = useState(false);
  const navigate = useNavigate();

  const handleMenuOpen = event => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleEditProfile = () => {
    navigate('/edycja-danych');
    handleMenuClose();
  };

  const handleLogoutClick = () => {
    handleLogout();
    handleMenuClose();
  };

  const handleDeleteAccount = () => {
    setModalOpen(true);
    handleMenuClose();
  };

  const confirmDeleteAccount = async () => {
    setModalOpen(false);
    setIsLoading(true);
    const status = await deleteAccount();
    if (status === 204) {
      handleLogout();
      navigate('/');
      addAlert('Pomyślnie usunięto konto.', 'success');
    } else {
      addAlert('Wystąpił nieoczekiwany błąd', 'error');
    }
    setIsLoading(false);
  };

  useEffect(() => {
    const fetchUserDetails = async () => {
      setIsLoading(true);
      const userData = await details();
      setUser(userData);
      setIsLoading(false);
    };

    if (isAuthenticated) {
      fetchUserDetails();
    }
  }, [isAuthenticated, setIsLoading]);

  return (
    <>
      <IconButton
        size="large"
        edge="end"
        color="inherit"
        onClick={handleMenuOpen}
        sx={{
          transition: 'transform 0.2s, background-color 0.2s',
          '&:hover': {
            backgroundColor: 'rgba(255, 255, 255, 0.1)',
            transform: 'scale(1.1)',
          },
        }}>
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
        {role !== 'EMPLOYER' && (
          <MenuItem
            onClick={handleEditProfile}
            sx={{ color: 'custom.50', textAlign: 'center' }}>
            Edytuj dane
          </MenuItem>
        )}
        {role === 'CUSTOMER' && (
          <MenuItem
            onClick={handleDeleteAccount}
            sx={{ color: 'red', textAlign: 'center' }}>
            Usuń konto
          </MenuItem>
        )}
        <MenuItem
          onClick={handleLogoutClick}
          sx={{ color: 'custom.50', textAlign: 'center' }}>
          Wyloguj się
        </MenuItem>
      </Menu>

      <ConfirmationModal
        open={isModalOpen}
        title="Usuń konto"
        description="Czy na pewno chcesz usunąć swoje konto? Tej operacji nie można cofnąć."
        onConfirm={confirmDeleteAccount}
        onCancel={() => setModalOpen(false)}
      />
    </>
  );
};

export default UserProfile;
