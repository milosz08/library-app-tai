import { useEffect, useState } from 'react';
import {
  Box,
  Container,
  MenuItem,
  Pagination,
  Select,
  TextField,
} from '@mui/material';
import { fetchRentalBooks } from '../../../api/rentalApi';
import LoanBooksTable from '../../../components/customer/LoanBooksTable';
import { useAlert } from '../../../hooks/useAlert';

const LoanBooksPage = () => {
  const [books, setBooks] = useState([]);
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(5);
  const [totalPages, setTotalPages] = useState(1);
  const [selectedBooks, setSelectedBooks] = useState([]);
  const [searchTitle, setSearchTitle] = useState('');
  const { addAlert } = useAlert();

  const loanBooks = async () => {
    try {
      const { data, totalPages } = await fetchRentalBooks(
        page,
        pageSize,
        searchTitle
      );
      setBooks(data);
      setTotalPages(totalPages);
    } catch (error) {
      addAlert('Nie udało się załadować książek.', 'error');
    }
  };

  const handlePageChange = (_, value) => {
    setPage(value);
  };

  const handlePageSizeChange = event => {
    setPageSize(event.target.value);
    setPage(1);
  };

  const toggleBookSelection = id => {
    setSelectedBooks(prev =>
      prev.includes(id)
        ? prev.filter(selectedId => selectedId !== id)
        : [...prev, id]
    );
  };

  const handleSearchChange = event => {
    setSearchTitle(event.target.value);
  };

  const handleSearchSubmit = () => {
    setPage(1);
    loanBooks();
  };

  useEffect(() => {
    loanBooks();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [page, pageSize]);

  return (
    <Container
      maxWidth="md"
      sx={{
        bgcolor: 'custom.900',
        padding: 3,
        borderRadius: 2,
        mt: 5,
        boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
      }}>
      <Box
        sx={{
          display: 'flex',
          flexWrap: 'wrap',
          alignItems: 'center',
          justifyContent: 'space-between',
          gap: 2,
          paddingY: 2,
        }}>
        <TextField
          label="Szukaj książki"
          value={searchTitle}
          onChange={handleSearchChange}
          onKeyUp={e => {
            if (e.key === 'Enter') {
              handleSearchSubmit();
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
        <Select
          value={pageSize}
          onChange={handlePageSizeChange}
          sx={{
            flex: '1 1 auto',
            maxWidth: '150px',
            bgcolor: 'custom.800',
            color: 'white',
            height: '40px',
            '& .MuiSelect-icon': { color: 'white' },
          }}>
          {[5, 10, 20, 50].map(size => (
            <MenuItem key={size} value={size}>
              {size}
            </MenuItem>
          ))}
        </Select>
      </Box>
      <LoanBooksTable
        books={books}
        onToggleSelection={toggleBookSelection}
        selectedBooks={selectedBooks}
        refreshBooks={loanBooks}
      />
      <Pagination
        count={totalPages}
        page={page}
        onChange={handlePageChange}
        sx={{
          mt: 2,
          display: 'flex',
          justifyContent: 'center',
          '& .MuiPaginationItem-root': { color: 'white' },
          '& .Mui-selected': { backgroundColor: 'custom.500' },
        }}
      />
    </Container>
  );
};

export default LoanBooksPage;
