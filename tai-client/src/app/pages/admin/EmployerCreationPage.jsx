import { useState } from 'react';
import { Box, Button, Container, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { createEmployer } from '~/api/employerApi';
import FormRedirectBox from '~/components/common/FormRedirectBox';
import FormTextField from '~/components/common/FormTextField';
import { useAlert } from '~/hooks/useAlert';
import { useLoader } from '~/hooks/useLoader';

const EmployerCreationPage = () => {
  const [form, setForm] = useState({
    firstName: '',
    lastName: '',
    email: '',
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

    const response = await createEmployer({
      firstName: form.firstName,
      lastName: form.lastName,
      email: form.email,
    });

    if (response.success) {
      addAlert(
        'Konto pracownika zostało utworzone. Na podany adres email zostały wysłane dalsze instrukcje.',
        'info'
      );
      navigate('/admin/pracownicy');
    } else if (response.errors) {
      Object.entries(response.errors).forEach(([field, message]) => {
        addAlert('Błąd w polu ' + field + ': ' + message, 'error');
      });
    } else {
      addAlert('Nie udało się utworzyć pracownika. Spróbuj ponownie.', 'error');
    }

    setIsLoading(false);
  };

  return (
    <Container
      maxWidth="sm"
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
          Tworzenie Pracownika
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
            <Box flexBasis="100%">
              <FormTextField
                label="Imię"
                name="firstName"
                value={form.firstName}
                onChange={handleChange}
              />
            </Box>
            <Box flexBasis="100%">
              <FormTextField
                label="Nazwisko"
                name="lastName"
                value={form.lastName}
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
            Utwórz Pracownika
          </Button>
          <FormRedirectBox
            questionText="Chcesz wrócić do listy?"
            linkText="Zobacz pracowników"
            linkPath="/admin/pracownicy"
          />
        </Box>
      </Box>
    </Container>
  );
};

export default EmployerCreationPage;
