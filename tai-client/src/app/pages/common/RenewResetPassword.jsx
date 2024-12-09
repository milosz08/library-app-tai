import { useState } from 'react';
import { Box, Button, Container, Typography } from '@mui/material';
import { useParams } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import { renewPassword } from '../../../api/passwordApi';
import FormTextField from '../../../components/common/FormTextField';
import { useAlert } from '../../../hooks/useAlert';
import { useLoader } from '../../../hooks/useLoader';

const RenewResetPassword = () => {
  const { token } = useParams();
  const [form, setForm] = useState({
    newPassword: '',
    confirmedNewPassword: '',
  });

  const { setIsLoading } = useLoader();
  const { addAlert } = useAlert();
  const navigate = useNavigate();
  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async e => {
    e.preventDefault();

    if (form.newPassword !== form.confirmedNewPassword) {
      addAlert('Hasła nie są zgodne.', 'error');
      return;
    }

    setIsLoading(true);
    const response = await renewPassword(token, form);
    if (response.success) {
      navigate('/logowanie');
      addAlert('Hasło zostało zresetowane pomyślnie.', 'success');
    } else {
      addAlert(response.message, 'error');
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
          Ustaw nowe hasło
        </Typography>
        <Box
          component="form"
          onSubmit={handleSubmit}
          display="flex"
          flexWrap="wrap"
          gap={2}
          sx={{ mt: 3 }}>
          <FormTextField
            label="Nowe hasło"
            name="newPassword"
            type="password"
            value={form.newPassword}
            onChange={handleChange}
            autoComplete="new-password"
          />
          <FormTextField
            label="Powtórz nowe hasło"
            name="confirmedNewPassword"
            type="password"
            value={form.confirmedNewPassword}
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
            Zmień hasło
          </Button>
        </Box>
      </Box>
    </Container>
  );
};

export default RenewResetPassword;
