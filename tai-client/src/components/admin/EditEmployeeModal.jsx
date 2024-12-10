import { useState } from 'react';
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  TextField,
} from '@mui/material';
import PropTypes from 'prop-types';

const EditEmployeeModal = ({
  open,
  onClose,
  onSubmit,
  initialFirstName,
  initialLastName,
}) => {
  const [firstName, setFirstName] = useState(initialFirstName || '');
  const [lastName, setLastName] = useState(initialLastName || '');

  const handleSubmit = () => {
    onSubmit({ firstName, lastName });
  };

  return (
    <Dialog
      open={open}
      onClose={onClose}
      PaperProps={{
        sx: {
          position: 'absolute',
          top: 20,
          margin: 0,
          backgroundColor: 'custom.700',
          color: 'custom.50',
        },
      }}>
      <DialogTitle
        sx={{
          backgroundColor: 'custom.700',
          color: 'custom.50',
          fontWeight: 'bold',
        }}>
        Edytuj dane pracownika
      </DialogTitle>
      <DialogContent
        sx={{
          display: 'flex',
          flexDirection: 'column',
          gap: 2,
          justifyContent: 'center',
          minHeight: '150px',
        }}>
        <TextField
          label="Imię"
          variant="outlined"
          value={firstName}
          onChange={e => setFirstName(e.target.value)}
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
        <TextField
          label="Nazwisko"
          variant="outlined"
          value={lastName}
          onChange={e => setLastName(e.target.value)}
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
      </DialogContent>
      <DialogActions
        sx={{
          backgroundColor: 'custom.700',
        }}>
        <Button
          onClick={onClose}
          sx={{
            color: 'white',
            backgroundColor: '#d32f2f',
            '&:hover': {
              backgroundColor: '#9e2b2b',
            },
          }}>
          Anuluj
        </Button>
        <Button
          onClick={handleSubmit}
          sx={{
            backgroundColor: '#2e7d32',
            color: '#fff',
            '&:hover': {
              backgroundColor: '#1b5e20',
            },
          }}>
          Zapisz
        </Button>
      </DialogActions>
    </Dialog>
  );
};

EditEmployeeModal.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
  initialFirstName: PropTypes.string,
  initialLastName: PropTypes.string,
};

export default EditEmployeeModal;
