import { Box, Link, Typography } from '@mui/material';
import PropTypes from 'prop-types';
import { NavLink } from 'react-router-dom';

const FormRedirectBox = ({ questionText, linkText, linkPath }) => {
  return (
    <Box
      sx={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        mt: 3,
        color: 'custom.200',
      }}>
      <Typography sx={{ mt: 2, textAlign: 'center', color: 'custom.200' }}>
        {questionText}{' '}
        <Link component={NavLink} to={linkPath} sx={{ color: 'custom.400' }}>
          {linkText}
        </Link>
      </Typography>
    </Box>
  );
};

FormRedirectBox.propTypes = {
  questionText: PropTypes.string.isRequired,
  linkText: PropTypes.string.isRequired,
  linkPath: PropTypes.string.isRequired,
};

export default FormRedirectBox;
