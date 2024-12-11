import { useState } from 'react';
import {
  Button,
  IconButton,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from '@mui/material';
import PropTypes from 'prop-types';
import { IoIosInformationCircleOutline } from 'react-icons/io';
import { fetchRentedDetails, returnBook } from '../../api/rentalApi';
import { useAlert } from '../../hooks/useAlert';
import BookDetailsModal from './BookDetailsModal';
import LoanBookModal from './LoanBookModal';

const LoanBooksTable = ({ books, refreshBooks }) => {
  const { addAlert } = useAlert();
  const [detailsModalOpen, setDetailsModalOpen] = useState(false);
  const [returnModalOpen, setReturnModalOpen] = useState(false);
  const [currentBook, setCurrentBook] = useState(null);

  const handleDetailsClick = async book => {
    const response = await fetchRentedDetails(book.id);
    if (!response.success) addAlert(response.message, 'error');
    else {
      setCurrentBook(response.data);
      setDetailsModalOpen(true);
    }
  };

  const handleReturnClick = book => {
    setCurrentBook(book);
    setReturnModalOpen(true);
  };

  const handleModalClose = () => {
    setDetailsModalOpen(false);
    setCurrentBook(null);
  };

  const handleReturnModalClose = () => {
    setReturnModalOpen(false);
    setCurrentBook(null);
  };

  const handleLoanConfirm = async quantity => {
    const response = await returnBook(currentBook.id, quantity);
    if (response == 204) {
      addAlert(
        'Zwrócono ' + quantity + ' kopii książki: ' + currentBook.title,
        'success'
      );
      setReturnModalOpen(false);
      refreshBooks();
    } else addAlert(response, 'error');
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
              <TableCell sx={{ color: 'white' }}>Tytuł</TableCell>
              <TableCell sx={{ color: 'white' }}>Wypożyczono</TableCell>
              <TableCell sx={{ color: 'white' }}>Szczegóły</TableCell>
              <TableCell sx={{ color: 'white' }}>Zwróć</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {books.map(book => (
              <TableRow key={book.id}>
                <TableCell sx={{ color: 'white' }}>{book.title}</TableCell>
                <TableCell sx={{ color: 'white' }}>
                  {book.rentedCopies}
                </TableCell>
                <TableCell>
                  <IconButton
                    onClick={() => handleDetailsClick(book)}
                    sx={{
                      color: 'white',
                      '&:hover': { color: 'blue' },
                    }}>
                    <IoIosInformationCircleOutline />
                  </IconButton>
                </TableCell>
                <TableCell>
                  <Button
                    variant="contained"
                    color="error"
                    onClick={() => handleReturnClick(book)}>
                    Zwróć
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      {currentBook && (
        <BookDetailsModal
          open={detailsModalOpen}
          onClose={handleModalClose}
          bookDetails={currentBook}
          rented={true}
        />
      )}
      {currentBook && (
        <LoanBookModal
          open={returnModalOpen}
          title="Zwróć książkę"
          onClose={handleReturnModalClose}
          bookDetails={currentBook}
          onConfirm={handleLoanConfirm}
        />
      )}
    </>
  );
};

LoanBooksTable.propTypes = {
  books: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
      title: PropTypes.string.isRequired,
      rentedCopies: PropTypes.number.isRequired,
    })
  ).isRequired,
  refreshBooks: PropTypes.func.isRequired,
};

export default LoanBooksTable;
