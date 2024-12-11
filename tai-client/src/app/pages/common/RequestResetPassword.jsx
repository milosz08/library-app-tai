import { useState } from 'react';
import { Box, Button, Container, Typography } from '@mui/material';
import { resetPassword } from '~/api/passwordApi';
import FormRedirectBox from '~/components/common/FormRedirectBox';
import FormTextField from '~/components/common/FormTextField';
import { useAlert } from '~/hooks/useAlert';
import { useLoader } from '~/hooks/useLoader';

const RequestResetPassword = () => {
  const [email, setEmail] = useState('');
  const { setIsLoading } = useLoader();
  const { addAlert } = useAlert();

  const handleSubmit = async e => {
    e.preventDefault();
    setIsLoading(true);
    const response = await resetPassword(email);
    if (response.success) {
      const activationLink = window.location + '/' + response.message;
      const alertMessage = 'Link przypominający hasło: ' + activationLink;
      addAlert(alertMessage, 'info');
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
          Przypomnij hasło
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
            value={email}
            onChange={e => setEmail(e.target.value)}
            autoComplete="email"
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
            Wyślij przypomnienie
          </Button>
        </Box>
        <FormRedirectBox
          questionText=""
          linkText="Wróć do logowania"
          linkPath="/przypomnij-haslo"
        />
      </Box>
    </Container>
  );
};

export default RequestResetPassword;
