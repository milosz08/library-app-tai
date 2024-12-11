import { useState } from 'react';
import {
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  TextField,
  Typography,
} from '@mui/material';
import PropTypes from 'prop-types';

const LoanBookModal = ({ open, onClose, bookDetails, onConfirm, title }) => {
  const [quantity, setQuantity] = useState(1);

  const handleQuantityChange = event => {
    const value = parseInt(event.target.value, 10);
    setQuantity(value);
  };

  const handleConfirm = () => {
    onConfirm(quantity);
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
          minWidth: '400px',
        },
      }}>
      <DialogTitle
        sx={{
          backgroundColor: 'custom.700',
          color: 'custom.50',
          fontWeight: 'bold',
        }}>
        {title}
      </DialogTitle>
      <DialogContent
        sx={{
          display: 'flex',
          flexDirection: 'column',
          gap: 2,
          justifyContent: 'center',
        }}>
        <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
          <Typography variant="body1">
            <strong>Tytuł:</strong> {bookDetails.title}
          </Typography>
          <Typography variant="body1">
            <strong>Dostępne kopie:</strong>{' '}
            {bookDetails.availableCopies || bookDetails.rentedCopies}
          </Typography>
          <TextField
            type="number"
            label="Liczba książek"
            value={quantity}
            onChange={handleQuantityChange}
            sx={{
              input: { color: 'white' },
              mt: 2,
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
        </Box>
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
          onClick={handleConfirm}
          sx={{
            color: 'white',
            backgroundColor: '#4caf50',
            '&:hover': {
              backgroundColor: '#388e3c',
            },
          }}>
          Potwierdź
        </Button>
      </DialogActions>
    </Dialog>
  );
};

LoanBookModal.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  bookDetails: PropTypes.shape({
    title: PropTypes.string,
    availableCopies: PropTypes.number,
    rentedCopies: PropTypes.number,
  }).isRequired,
  onConfirm: PropTypes.func.isRequired,
  title: PropTypes.string,
};

export default LoanBookModal;
