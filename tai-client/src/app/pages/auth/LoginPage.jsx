import { useState } from 'react';
import { Box, Button, Container, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import FormRedirectBox from '../../../components/common/FormRedirectBox';
import FormTextField from '../../../components/common/FormTextField';
import { useAlert } from '../../../hooks/useAlert';
import { useAuth } from '../../../hooks/useAuth';
import { useLoader } from '../../../hooks/useLoader';

const LoginPage = () => {
  const [form, setForm] = useState({
    email: '',
    password: '',
  });
  const { setIsLoading } = useLoader();
  const { handleLogin } = useAuth();
  const { addAlert } = useAlert();
  const navigate = useNavigate();

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async e => {
    e.preventDefault();
    setIsLoading(true);
    const result = await handleLogin(form.email, form.password);
    if (result.success) {
      addAlert('Zalogowano pomyślnie!', 'success');
      navigate('/');
    } else if (result.token) {
      const activationLink =
        window.location.origin + '/aktywacja/' + result.token;
      const activationMessage =
        'Twoje konto wymaga aktywacji. Kliknij poniższy link, aby je aktywować:' +
        activationLink;
      addAlert(activationMessage, 'info');
    } else {
      addAlert(result.error || 'Nieprawidłowe dane logowania.', 'error');
    }
    setIsLoading(false);
  };

  return (
    <Container
      maxWidth="xs"
      sx={{
        bgcolor: 'custom.900',
        padding: 3,
        borderRadius: 2,
        mt: 5,
        boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
      }}>
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          marginTop: 4,
        }}>
        <Typography variant="h5" sx={{ color: 'white' }}>
          Logowanie
        </Typography>
        <Box
          component="form"
          onSubmit={handleSubmit}
          display="flex"
          flexWrap="wrap"
          gap={2}
          sx={{ mt: 3 }}>
          <FormTextField
            label="Email"
            name="email"
            type="email"
            value={form.email}
            onChange={handleChange}
            autoComplete="email"
          />
          <FormTextField
            label="Hasło"
            name="password"
            type="password"
            value={form.password}
            onChange={handleChange}
            autoComplete="new-password"
          />
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{
              mt: 2,
              color: 'white',
              bgcolor: 'customBlue.600',
              '&:hover': { bgcolor: 'customBlue.500' },
            }}>
            Zaloguj się
          </Button>
        </Box>
        <FormRedirectBox
          questionText="Nie masz konta?"
          linkText="Zarejestruj się"
          linkPath="/rejestracja"
        />
      </Box>
    </Container>
  );
};

export default LoginPage;
