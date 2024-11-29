import { TextField } from '@mui/material';
import PropTypes from 'prop-types';

const FormTextField = ({
  label,
  name,
  type = 'text',
  value,
  onChange,
  required = false,
  ...props
}) => {
  return (
    <TextField
      variant="outlined"
      fullWidth
      label={label}
      name={name}
      type={type}
      value={value}
      onChange={onChange}
      required={required}
      {...props}
      sx={{
        bgcolor: 'custom.700',
        input: { color: 'white' },
        '& .MuiInputLabel-root': { color: 'custom.400' },
        '& .MuiOutlinedInput-root': {
          '& fieldset': {
            borderColor: 'custom.700',
          },
          '&:hover fieldset': {
            borderColor: 'custom.600',
          },
          '&.Mui-focused fieldset': {
            borderColor: 'customBlue.500',
          },
        },
      }}
    />
  );
};

FormTextField.propTypes = {
  label: PropTypes.string.isRequired,
  name: PropTypes.string.isRequired,
  type: PropTypes.string,
  value: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired,
  required: PropTypes.bool,
};

export default FormTextField;
