import { useState } from 'react';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import {
  Checkbox,
  IconButton,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from '@mui/material';
import PropTypes from 'prop-types';
import { fetchBookDetails, updateBook } from '~/api/bookApi';
import { useAlert } from '~/hooks/useAlert';
import EditBookModal from './EditBookModal';

const EmployerBooksTable = ({
  books,
  onDelete,
  onToggleSelection,
  selectedBooks,
  refreshData,
}) => {
  const { addAlert } = useAlert();
  const [editModalOpen, setEditModalOpen] = useState(false);
  const [currentBook, setCurrentBook] = useState(null);
  const [currentBookId, setCurrentBookId] = useState(null);

  const handleEditClick = async book => {
    try {
      const response = await fetchBookDetails(book.id);
      setCurrentBookId(book.id);
      setCurrentBook(response);
      setEditModalOpen(true);
    } catch (error) {
      addAlert(error.message, 'error');
    }
  };

  const handleModalClose = () => {
    setEditModalOpen(false);
    setCurrentBook(null);
  };

  const handleModalSubmit = async updatedData => {
    if (!currentBook) return;

    const response = await updateBook(currentBookId, updatedData);

    if (response.success) {
      addAlert('Książka została zaktualizowana pomyślnie.', 'success');
      handleModalClose();
      refreshData();
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
      addAlert('Wystąpił nieoczekiwany błąd.', 'error');
    }
  };

  return (
    <>
      <TableContainer
        sx={{
          bgcolor: 'custom.800',
          borderRadius: 2,
          boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
        }}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell sx={{ color: 'white' }}>ID</TableCell>
              <TableCell sx={{ color: 'white' }}>Tytuł</TableCell>
              <TableCell sx={{ color: 'white' }}>Ilość</TableCell>
              <TableCell sx={{ color: 'white' }}>Dostępne</TableCell>
              <TableCell sx={{ color: 'white' }}>Wybierz</TableCell>
              <TableCell sx={{ color: 'white' }}>Usuń</TableCell>
              <TableCell sx={{ color: 'white' }}>Edytuj</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {books.map(book => (
              <TableRow key={book.id}>
                <TableCell sx={{ color: 'white' }}>{book.id}</TableCell>
                <TableCell sx={{ color: 'white' }}>{book.title}</TableCell>
                <TableCell sx={{ color: 'white' }}>{book.allCopies}</TableCell>
                <TableCell sx={{ color: 'white' }}>
                  {book.availableCopies}
                </TableCell>
                <TableCell>
                  <Checkbox
                    checked={selectedBooks.includes(book.id)}
                    onChange={() => onToggleSelection(book.id)}
                    sx={{
                      color: 'white',
                      '&.Mui-checked': { color: 'custom.500' },
                    }}
                  />
                </TableCell>
                <TableCell>
                  <IconButton
                    onClick={() => onDelete(book.id)}
                    sx={{
                      color: 'white',
                      '&:hover': { color: 'red' },
                    }}>
                    <DeleteIcon />
                  </IconButton>
                </TableCell>
                <TableCell>
                  <IconButton
                    onClick={() => handleEditClick(book)}
                    sx={{
                      color: 'white',
                      '&:hover': { color: 'blue' },
                    }}>
                    <EditIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {currentBook && (
        <EditBookModal
          open={editModalOpen}
          onClose={handleModalClose}
          onSubmit={handleModalSubmit}
          initialData={currentBook.data}
        />
      )}
    </>
  );
};

EmployerBooksTable.propTypes = {
  books: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
      title: PropTypes.string.isRequired,
      allCopies: PropTypes.number.isRequired,
      availableCopies: PropTypes.number.isRequired,
    })
  ).isRequired,
  onDelete: PropTypes.func.isRequired,
  onToggleSelection: PropTypes.func.isRequired,
  refreshData: PropTypes.func,
  selectedBooks: PropTypes.arrayOf(
    PropTypes.oneOfType([PropTypes.string, PropTypes.number])
  ).isRequired,
};

export default EmployerBooksTable;
