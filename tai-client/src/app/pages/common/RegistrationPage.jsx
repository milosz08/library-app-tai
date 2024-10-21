import { useState } from 'react';
import { Box, Button, Container, Typography } from '@mui/material';
import FormRedirectBox from '../../../components/common/FormRedirectBox';
import FormTextField from '../../../components/common/FormTextField';

const RegistrationPage = () => {
  const [form, setForm] = useState({
    firstName: '',
    lastName: '',
    city: '',
    postalCode: '',
    street: '',
    houseNumber: '',
    apartmentNumber: '',
    email: '',
    password: '',
  });

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = e => {
    e.preventDefault();
    console.log(form);
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
          <Box display="flex" flexWrap="wrap" gap={2}>
            <Box flexBasis="49%">
              <FormTextField
                label="Imię"
                name="firstName"
                value={form.firstName}
                onChange={handleChange}
              />
            </Box>
            <Box flexBasis="49%">
              <FormTextField
                label="Nazwisko"
                name="lastName"
                value={form.lastName}
                onChange={handleChange}
              />
            </Box>
            <Box flexBasis="49%">
              <FormTextField
                label="Miejscowość"
                name="city"
                value={form.city}
                onChange={handleChange}
              />
            </Box>
            <Box flexBasis="49%">
              <FormTextField
                label="Kod pocztowy"
                name="postalCode"
                value={form.postalCode}
                onChange={handleChange}
              />
            </Box>
            <Box flexBasis="49%">
              <FormTextField
                label="Ulica"
                name="street"
                value={form.street}
                onChange={handleChange}
              />
            </Box>
            <Box flexBasis="23.5%">
              <FormTextField
                label="Numer domu"
                name="houseNumber"
                value={form.houseNumber}
                onChange={handleChange}
              />
            </Box>
            <Box flexBasis="23.5%">
              <FormTextField
                label="Numer mieszkania"
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
              />
            </Box>
            <Box flexBasis="100%">
              <FormTextField
                label="Hasło"
                name="password"
                type="password"
                value={form.password}
                onChange={handleChange}
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
