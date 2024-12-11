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
import { fetchBookDetails } from '~/api/bookApi';
import { rentBook } from '~/api/rentalApi';
import { useAlert } from '~/hooks/useAlert';
import BookDetailsModal from './BookDetailsModal';
import LoanBookModal from './LoanBookModal';

const CustomerBooksTable = ({ books, refreshBooks }) => {
  const { addAlert } = useAlert();
  const [detailsModalOpen, setDetailsModalOpen] = useState(false);
  const [loanModalOpen, setLoanModalOpen] = useState(false);
  const [currentBook, setCurrentBook] = useState(null);

  const handleDetailsClick = async book => {
    const response = await fetchBookDetails(book.id);
    if (!response.success) addAlert(response.message, 'error');
    else {
      setCurrentBook(response.data);
      setDetailsModalOpen(true);
    }
  };

  const handleLoanClick = book => {
    setCurrentBook(book);
    setLoanModalOpen(true);
  };

  const handleModalClose = () => {
    setDetailsModalOpen(false);
    setCurrentBook(null);
  };

  const handleLoanModalClose = () => {
    setLoanModalOpen(false);
    setCurrentBook(null);
  };

  const handleLoanConfirm = async quantity => {
    const response = await rentBook(currentBook.id, quantity);
    if (response == 204) {
      addAlert(
        'Wypożyczono ' + quantity + ' kopie książki: ' + currentBook.title,
        'success'
      );
      setLoanModalOpen(false);
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
              <TableCell sx={{ color: 'white' }}>ID</TableCell>
              <TableCell sx={{ color: 'white' }}>Tytuł</TableCell>
              <TableCell sx={{ color: 'white' }}>Ilość</TableCell>
              <TableCell sx={{ color: 'white' }}>Dostępne</TableCell>
              <TableCell sx={{ color: 'white' }}>Szczegóły</TableCell>
              <TableCell sx={{ color: 'white' }}>Wypożycz</TableCell>
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
                  {book.availableCopies > 0 && (
                    <Button
                      variant="contained"
                      color="primary"
                      onClick={() => handleLoanClick(book)}>
                      Wypożycz
                    </Button>
                  )}
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
        />
      )}
      {currentBook && (
        <LoanBookModal
          open={loanModalOpen}
          title="Wypożycz książkę"
          onClose={handleLoanModalClose}
          bookDetails={currentBook}
          onConfirm={handleLoanConfirm}
        />
      )}
    </>
  );
};

CustomerBooksTable.propTypes = {
  books: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
      title: PropTypes.string.isRequired,
      allCopies: PropTypes.number.isRequired,
      availableCopies: PropTypes.number.isRequired,
    })
  ).isRequired,
  refreshBooks: PropTypes.func.isRequired,
};

export default CustomerBooksTable;
