import { TextField } from '@mui/material';
import PropTypes from 'prop-types';

const EmployeeTextField = ({ label, value, onChange }) => (
  <TextField
    label={label}
    variant="outlined"
    value={value}
    onChange={e => onChange(e.target.value)}
    fullWidth
    sx={{
      input: { color: 'white' },
      label: { color: 'custom.200' },
      '& .MuiOutlinedInput-root': {
        '& fieldset': {
          borderColor: 'custom.200',
        },
        '&:hover fieldset': {
          borderColor: 'white',
        },
      },
    }}
  />
);

EmployeeTextField.propTypes = {
  label: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired,
};

export default EmployeeTextField;
