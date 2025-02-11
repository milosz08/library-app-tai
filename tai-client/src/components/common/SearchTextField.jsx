import { TextField } from '@mui/material';
import PropTypes from 'prop-types';

const SearchTextField = ({ label, value, onChange, onSubmit }) => (
  <TextField
    label={label}
    value={value}
    onChange={onChange}
    onKeyUp={e => {
      if (e.key === 'Enter') {
        onSubmit();
      }
    }}
    sx={{
      flex: '1 1 auto',
      bgcolor: 'custom.800',
      color: 'white',
      '& .MuiInputBase-input': { color: 'white' },
      '& .MuiInputLabel-root': { color: 'white' },
    }}
  />
);

SearchTextField.propTypes = {
  label: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
};

export default SearchTextField;
