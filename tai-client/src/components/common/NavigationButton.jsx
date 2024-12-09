import { Button } from '@mui/material';
import PropTypes from 'prop-types';
import { useNavigate } from 'react-router';

const NavigationButton = ({ label, path }) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(path);
  };

  return (
    <Button
      color="inherit"
      onClick={handleClick}
      sx={{
        textTransform: 'none',
        position: 'relative',
        '&:hover': {
          '&::after': {
            transform: 'scaleX(1)',
          },
        },
        '&::after': {
          content: '""',
          position: 'absolute',
          bottom: -2,
          left: 0,
          width: '100%',
          height: 2,
          backgroundColor: 'white',
          transform: 'scaleX(0)',
          transformOrigin: 'center',
          transition: 'transform 0.3s ease-in-out',
        },
      }}>
      {label}
    </Button>
  );
};

NavigationButton.propTypes = {
  label: PropTypes.string.isRequired,
  path: PropTypes.string.isRequired,
};

export default NavigationButton;
