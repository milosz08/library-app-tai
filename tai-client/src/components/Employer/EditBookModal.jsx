import { useState } from 'react';
import CloseIcon from '@mui/icons-material/Close';
import {
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  IconButton,
  TextField,
  Typography,
} from '@mui/material';
import PropTypes from 'prop-types';

const EditBookModal = ({ open, onClose, onSubmit, initialData }) => {
  const [form, setForm] = useState({
    title: initialData?.title || '',
    year: initialData?.year || '',
    publisher: initialData?.publisher || '',
    city: initialData?.city || '',
    copies: initialData?.allCopies || '',
    authors: initialData?.authors || [{ firstName: '', lastName: '' }],
  });

  const handleChange = e => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  const handleAuthorChange = (index, e) => {
    const newAuthors = [...form.authors];
    newAuthors[index][e.target.name] = e.target.value;
    setForm({ ...form, authors: newAuthors });
  };

  const addAuthorField = () => {
    setForm({
      ...form,
      authors: [...form.authors, { firstName: '', lastName: '' }],
    });
  };

  const removeAuthorField = index => {
    const newAuthors = form.authors.filter((_, i) => i !== index);
    setForm({ ...form, authors: newAuthors });
  };

  const handleSubmit = () => {
    onSubmit(form);
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
        Edytuj książkę
      </DialogTitle>
      <DialogContent
        sx={{
          display: 'flex',
          flexDirection: 'column',
          gap: 2,
          justifyContent: 'center',
          minHeight: '600px',
        }}>
        <TextField
          label="Tytuł"
          name="title"
          value={form.title}
          onChange={handleChange}
          fullWidth
          sx={textFieldStyles}
        />
        <TextField
          label="Rok wydania"
          name="year"
          type="number"
          value={form.year}
          onChange={handleChange}
          fullWidth
          sx={textFieldStyles}
        />
        <TextField
          label="Wydawnictwo"
          name="publisher"
          value={form.publisher}
          onChange={handleChange}
          fullWidth
          sx={textFieldStyles}
        />
        <TextField
          label="Miejscowość"
          name="city"
          value={form.city}
          onChange={handleChange}
          fullWidth
          sx={textFieldStyles}
        />
        <TextField
          label="Liczba kopii"
          name="copies"
          type="number"
          value={form.copies}
          onChange={handleChange}
          fullWidth
          sx={textFieldStyles}
        />
        <Box sx={{ mt: 2, maxHeight: '180px', overflow: 'auto' }}>
          <Typography variant="h6" sx={{ color: 'custom.200', mb: 2 }}>
            Autorzy
          </Typography>
          {form.authors.map((author, index) => (
            <Box
              key={index}
              sx={{
                display: 'flex',
                gap: 2,
                alignItems: 'center',
                mt: index > 0 ? 2 : 0,
              }}>
              <TextField
                label="Imię"
                name="firstName"
                value={author.firstName}
                onChange={e => handleAuthorChange(index, e)}
                sx={textFieldStyles}
              />
              <TextField
                label="Nazwisko"
                name="lastName"
                value={author.lastName}
                onChange={e => handleAuthorChange(index, e)}
                sx={textFieldStyles}
              />
              <IconButton
                aria-label="Usuń"
                color="error"
                onClick={() => removeAuthorField(index)}>
                <CloseIcon />
              </IconButton>
            </Box>
          ))}
          <Button
            variant="text"
            sx={{
              mt: 2,
              color: 'custom.200',
              textTransform: 'none',
              border: 1,
              borderRadius: 2,
              '&:hover': { bgcolor: 'custom.400' },
            }}
            onClick={addAuthorField}>
            Dodaj autora
          </Button>
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

const textFieldStyles = {
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
};

EditBookModal.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
  initialData: PropTypes.shape({
    title: PropTypes.string,
    year: PropTypes.number,
    publisher: PropTypes.string,
    city: PropTypes.string,
    allCopies: PropTypes.number,
    authors: PropTypes.arrayOf(
      PropTypes.shape({
        firstName: PropTypes.string,
        lastName: PropTypes.string,
      })
    ),
  }),
};

export default EditBookModal;
