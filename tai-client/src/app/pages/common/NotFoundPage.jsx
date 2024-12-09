import { Button, Typography } from '@mui/material';
import { AiOutlineArrowLeft, AiOutlineWarning } from 'react-icons/ai';
import { useNavigate } from 'react-router-dom';

const NotFoundPage = () => {
  const navigate = useNavigate();

  const handleGoBack = () => {
    navigate('/');
  };

  return (
    <>
      <AiOutlineWarning
        size={80}
        style={{
          display: 'block',
          margin: '0 auto',
          color: 'white',
          marginBottom: 16,
        }}
      />
      <Typography
        variant="h3"
        sx={{ textAlign: 'center', color: 'white', marginBottom: 2 }}>
        404 - Strona nie znaleziona
      </Typography>
      <Typography
        variant="h6"
        sx={{ textAlign: 'center', color: 'white', marginBottom: 4 }}>
        Wygląda na to, że strona, której szukasz, nie istnieje.
      </Typography>
      <Button
        onClick={handleGoBack}
        variant="contained"
        startIcon={<AiOutlineArrowLeft />}
        sx={{
          display: 'block',
          margin: '0 auto',
          bgcolor: 'customBlue.600',
          color: 'white',
          '&:hover': { bgcolor: 'customBlue.500' },
        }}>
        Wróć na stronę główną
      </Button>
    </>
  );
};

export default NotFoundPage;
