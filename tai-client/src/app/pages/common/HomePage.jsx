import { Box, Button, Container, Grid2, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '~/hooks/useAuth';

const HomePage = () => {
  const { isAuthenticated, role } = useAuth();
  const navigate = useNavigate();

  const getQuickLinks = () => {
    if (!isAuthenticated) return [];

    switch (role) {
      case 'ADMIN':
        return [
          { label: 'Zarządzaj zdarzeniami', path: '/admin/zdarzenia' },
          { label: 'Dodaj pracownika', path: '/admin/dodaj-pracownika' },
          { label: 'Przeglądaj pracowników', path: '/admin/pracownicy' },
        ];
      case 'EMPLOYER':
        return [
          { label: 'Dodaj książkę', path: '/pracownik/dodaj-ksiazke' },
          { label: 'Zarządzaj książkami', path: '/pracownik/ksiazki' },
        ];
      case 'CUSTOMER':
        return [
          { label: 'Przeglądaj książki', path: '/ksiazki' },
          { label: 'Moje wypożyczenia', path: '/wypozyczone-ksiazki' },
        ];
      default:
        return [];
    }
  };

  const quickLinks = getQuickLinks();

  return (
    <Container
      maxWidth="lg"
      sx={{
        bgcolor: 'custom.900',
        color: 'white',
        borderRadius: 2,
        padding: 4,
        mt: 5,
        boxShadow: '0 6px 10px rgba(0, 0, 0, 0.15)',
      }}>
      <Box textAlign="center" mb={4}>
        <Typography variant="h4" sx={{ fontWeight: 'bold', mb: 2 }}>
          Witamy w bibliotece
        </Typography>
        {isAuthenticated && (
          <Typography variant="h6" sx={{ mt: 1 }}>
            Jesteś zalogowany jako: <strong>{role}</strong>
          </Typography>
        )}
        {!isAuthenticated && (
          <Typography variant="h6" sx={{ mt: 1 }}>
            Zaloguj się, aby uzyskać pełny dostęp do funkcji.
          </Typography>
        )}
      </Box>

      {quickLinks.length > 0 && (
        <Box>
          <Typography
            variant="h5"
            mb={3}
            sx={{ textAlign: 'center', fontWeight: 'medium' }}>
            Szybkie przejścia:
          </Typography>
          <Grid2 container spacing={3} justifyContent="center">
            {quickLinks.map(link => (
              <Grid2 xs={12} sm={6} md={4} key={link.label}>
                <Button
                  fullWidth
                  variant="contained"
                  sx={{
                    bgcolor: 'customBlue.600',
                    color: 'white',
                    fontWeight: 'bold',
                    padding: 2,
                    '&:hover': { bgcolor: 'customBlue.500' },
                    boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                  }}
                  onClick={() => navigate(link.path)}>
                  {link.label}
                </Button>
              </Grid2>
            ))}
          </Grid2>
        </Box>
      )}
    </Container>
  );
};

export default HomePage;
