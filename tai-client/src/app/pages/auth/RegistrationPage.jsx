import { useState } from 'react';
import { Box, Button, Container, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { register } from '~/api/authApi';
import FormRedirectBox from '~/components/common/FormRedirectBox';
import FormTextField from '~/components/common/FormTextField';
import { useAlert } from '~/hooks/useAlert';
import { useLoader } from '~/hooks/useLoader';

const RegistrationPage = () => {
  const [form, setForm] = useState({
    firstName: '',
    lastName: '',
    city: '',
    street: '',
    buildingNumber: '',
    apartmentNumber: '',
    email: '',
    password: '',
    confirmedPassword: '',
  });
  const { setIsLoading } = useLoader();
  const { addAlert } = useAlert();
  const navigate = useNavigate();

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async e => {
    e.preventDefault();
    setIsLoading(true);

    if (form.password !== form.confirmedPassword) {
      addAlert('Hasła nie są zgodne.', 'error');
      setIsLoading(false);
      return;
    }

    const response = await register({
      firstName: form.firstName,
      lastName: form.lastName,
      city: form.city,
      street: form.street,
      buildingNumber: form.buildingNumber,
      apartmentNumber: form.apartmentNumber || null,
      email: form.email,
      password: form.password,
      confirmedPassword: form.confirmedPassword,
    });

    if (response.success) {
      addAlert(
        'Twoje konto zostało utworzone. System wysłał wiadomość na podany adres email umożliwiającą aktywację konta. Sprawdź pocztę.',
        'info'
      );
      navigate('/logowanie');
    } else if (response.errors) {
      Object.entries(response.errors).forEach(([, message]) => {
        addAlert(message, 'error');
      });
    } else {
      addAlert('Rejestracja nie powiodła się. Spróbuj ponownie.', 'error');
    }

    setIsLoading(false);
  };

  return (
    <Container
      maxWidth="md"
      sx={{
        bgcolor: 'custom.900',
        padding: 4,
        borderRadius: 2,
        mt: 5,
        boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
      }}>
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          marginTop: 2,
        }}>
        <Typography variant="h5" sx={{ color: 'white' }}>
          Rejestracja
        </Typography>
        <Box
          component="form"
          onSubmit={handleSubmit}
          sx={{ mt: 3, width: '100%' }}>
          <Box
            display="flex"
            flexWrap="wrap"
            gap={2}
            sx={{
              '@media (max-width:900px)': {
                flexDirection: 'column',
              },
            }}>
            <Box
              flexBasis="49%"
              sx={{
                flexBasis: { xs: '100%', sm: '49%' },
              }}>
              <FormTextField
                label="Imię"
                name="firstName"
                value={form.firstName}
                onChange={handleChange}
              />
            </Box>
            <Box
              flexBasis="49%"
              sx={{
                flexBasis: { xs: '100%', sm: '49%' },
              }}>
              <FormTextField
                label="Nazwisko"
                name="lastName"
                value={form.lastName}
                onChange={handleChange}
              />
            </Box>
            <Box
              flexBasis="49%"
              sx={{
                flexBasis: { xs: '100%', sm: '49%' },
              }}>
              <FormTextField
                label="Miejscowość"
                name="city"
                value={form.city}
                onChange={handleChange}
              />
            </Box>
            <Box
              flexBasis="49%"
              sx={{
                flexBasis: { xs: '100%', sm: '49%' },
              }}>
              <FormTextField
                label="Ulica"
                name="street"
                value={form.street}
                onChange={handleChange}
              />
            </Box>
            <Box
              flexBasis="49%"
              sx={{
                flexBasis: { xs: '100%', sm: '49%' },
              }}>
              <FormTextField
                label="Numer budynku"
                name="buildingNumber"
                value={form.buildingNumber}
                onChange={handleChange}
              />
            </Box>
            <Box
              flexBasis="49%"
              sx={{
                flexBasis: { xs: '100%', sm: '49%' },
              }}>
              <FormTextField
                label="Numer apartamentu (opcjonalne)"
                name="apartmentNumber"
                value={form.apartmentNumber}
                onChange={handleChange}
              />
            </Box>
            <Box flexBasis="100%">
              <FormTextField
                label="Email"
                name="email"
                type="email"
                value={form.email}
                onChange={handleChange}
                autoComplete="email"
              />
            </Box>
            <Box
              flexBasis="49%"
              sx={{
                flexBasis: { xs: '100%', sm: '49%' },
              }}>
              <FormTextField
                label="Hasło"
                name="password"
                type="password"
                value={form.password}
                onChange={handleChange}
                autoComplete="new-password"
              />
            </Box>
            <Box
              flexBasis="49%"
              sx={{
                flexBasis: { xs: '100%', sm: '49%' },
              }}>
              <FormTextField
                label="Potwierdź hasło"
                name="confirmedPassword"
                type="password"
                value={form.confirmedPassword}
                onChange={handleChange}
                autoComplete="new-password"
              />
            </Box>
          </Box>
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{
              mt: 3,
              bgcolor: 'customBlue.600',
              color: 'white',
              '&:hover': { bgcolor: 'customBlue.500' },
            }}>
            Zarejestruj się
          </Button>
          <FormRedirectBox
            questionText="Masz już konto?"
            linkText="Zaloguj się"
            linkPath="/logowanie"
          />
        </Box>
      </Box>
    </Container>
  );
};

export default RegistrationPage;
