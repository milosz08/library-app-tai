import { useState } from 'react';
import { Box, Button, Container, Typography } from '@mui/material';
import { useNavigate, useParams } from 'react-router-dom';
import { changePasswordOnFirstAccess } from '~/api/employerApi';
import FormTextField from '~/components/common/FormTextField';
import { useAlert } from '~/hooks/useAlert';
import { useLoader } from '~/hooks/useLoader';

const FirstAccessPage = () => {
  const { token } = useParams();
  const navigate = useNavigate();
  const { addAlert } = useAlert();
  const { setIsLoading } = useLoader();

  const [form, setForm] = useState({
    temporaryPassword: '',
    newPassword: '',
    confirmedNewPassword: '',
  });

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async e => {
    e.preventDefault();

    if (form.newPassword !== form.confirmedNewPassword) {
      addAlert('Nowe hasło i potwierdzenie muszą być takie same.', 'error');
      return;
    }

    setIsLoading(true);
    const result = await changePasswordOnFirstAccess(token, form);

    if (result.success) {
      addAlert(result.message, 'success');
      navigate('/logowanie');
    } else if (result.errors) {
      Object.entries(result.errors).forEach(([, message]) => {
        addAlert(message, 'error');
      });
    } else {
      addAlert('Wystąpił nieznany błąd. Spróbuj ponownie.', 'error');
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
          Aktywacja Konta
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
                label="Tymczasowe hasło"
                name="temporaryPassword"
                value={form.temporaryPassword}
                onChange={handleChange}
                type="password"
              />
            </Box>
            <Box flexBasis="100%">
              <FormTextField
                label="Nowe hasło"
                name="newPassword"
                value={form.newPassword}
                onChange={handleChange}
                type="password"
              />
            </Box>
            <Box flexBasis="100%">
              <FormTextField
                label="Potwierdź nowe hasło"
                name="confirmedNewPassword"
                value={form.confirmedNewPassword}
                onChange={handleChange}
                type="password"
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
            Zmień hasło i aktywuj konto
          </Button>
        </Box>
      </Box>
    </Container>
  );
};

export default FirstAccessPage;
