import { useState } from 'react';
import { Close as CloseIcon } from '@mui/icons-material';
import { Box, Button, Container, IconButton, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { addBook } from '../../../api/bookApi';
import FormTextField from '../../../components/common/FormTextField';
import { useAlert } from '../../../hooks/useAlert';
import { useLoader } from '../../../hooks/useLoader';

const AddBookPage = () => {
  const [form, setForm] = useState({
    title: '',
    year: '',
    publisher: '',
    city: '',
    copies: '',
    authors: [{ firstName: '', lastName: '' }],
  });
  const { setIsLoading } = useLoader();
  const { addAlert } = useAlert();
  const navigate = useNavigate();

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

  const handleSubmit = async e => {
    e.preventDefault();
    setIsLoading(true);

    const response = await addBook(form);

    if (response.success) {
      addAlert('Książka została dodana!', 'success');
      navigate('/pracownik/ksiazki');
    } else if (response.errors) {
      Object.entries(response.errors).forEach(([key, message]) => {
        if (key === 'authors' && typeof message === 'object') {
          Object.entries(message).forEach(([, authorError]) => {
            Object.entries(authorError).forEach(([, fieldMessage]) => {
              addAlert(fieldMessage, 'error');
            });
          });
        } else {
          addAlert(message, 'error');
        }
      });
    } else {
      addAlert('Dodanie książki nie powiodło się. Spróbuj ponownie.', 'error');
    }

    setIsLoading(false);
  };

  return (
    <Container
      maxWidth="md"
      sx={{
        bgcolor: 'custom.900',
        padding: 3,
        borderRadius: 2,
        boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
      }}>
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}>
        <Typography variant="h5" sx={{ color: 'white' }}>
          Dodaj książkę
        </Typography>
        <Box
          component="form"
          onSubmit={handleSubmit}
          sx={{
            display: 'flex',
            flexDirection: 'column',
            gap: 1,
            width: '100%',
            mt: 2,
          }}>
          <FormTextField
            label="Tytuł"
            name="title"
            value={form.title}
            onChange={handleChange}
          />
          <FormTextField
            label="Rok wydania"
            name="year"
            type="number"
            value={form.year}
            onChange={handleChange}
          />
          <FormTextField
            label="Wydawnictwo"
            name="publisher"
            value={form.publisher}
            onChange={handleChange}
          />
          <FormTextField
            label="Miejscowość"
            name="city"
            value={form.city}
            onChange={handleChange}
          />
          <FormTextField
            label="Liczba kopii"
            name="copies"
            type="number"
            value={form.copies}
            onChange={handleChange}
          />
          <Box sx={{ mt: 1 }}>
            <Typography variant="h6" sx={{ color: 'white', mb: 1 }}>
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
                <FormTextField
                  label="Imię autora"
                  name="firstName"
                  value={author.firstName}
                  onChange={e => handleAuthorChange(index, e)}
                />
                <FormTextField
                  label="Nazwisko autora"
                  name="lastName"
                  value={author.lastName}
                  onChange={e => handleAuthorChange(index, e)}
                />
                <IconButton
                  aria-label="Usuń autora"
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
                color: 'white',
                textTransform: 'none',
                border: 1,
                borderRadius: 2,
                '&:hover': { bgcolor: 'custom.400' },
              }}
              onClick={addAuthorField}>
              Dodaj autora
            </Button>
          </Box>
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{
              mt: 2,
              bgcolor: 'customBlue.600',
              color: 'white',
              '&:hover': { bgcolor: 'customBlue.500' },
            }}>
            Dodaj książkę
          </Button>
        </Box>
      </Box>
    </Container>
  );
};

export default AddBookPage;
