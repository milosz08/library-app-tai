import { useEffect } from 'react';
import { Box, CircularProgress, Container, Typography } from '@mui/material';
import { useNavigate, useParams } from 'react-router-dom';
import { activateAccount } from '~/api/authApi';
import { useAlert } from '~/hooks/useAlert';

const ActivationPage = () => {
  const { token } = useParams();
  const navigate = useNavigate();
  const { addAlert } = useAlert();

  useEffect(() => {
    const activate = async () => {
      const result = await activateAccount(token);

      if (result?.success) {
        addAlert('Konto zostało pomyślnie aktywowane!', 'success');
      } else {
        addAlert(result?.message, 'error');
      }

      navigate('/logowanie');
    };

    activate();
  }, [token, navigate, addAlert]);

  return (
    <Container
      maxWidth="sm"
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        text: 'white',
      }}>
      <Box textAlign="center">
        <Typography variant="h5" gutterBottom sx={{ color: 'white' }}>
          Aktywacja konta
        </Typography>
        <Typography variant="body1" gutterBottom sx={{ color: 'white' }}>
          Trwa aktywacja Twojego konta. Proszę czekać...
        </Typography>
        <CircularProgress sx={{ mt: 2, color: 'white' }} />
      </Box>
    </Container>
  );
};

export default ActivationPage;
