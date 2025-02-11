import { useEffect, useState } from 'react';
import { Box, Container, MenuItem, Select } from '@mui/material';
import { fetchBooks } from '~/api/bookApi';
import Paginator from '~/components/common/Paginator';
import SearchTextField from '~/components/common/SearchTextField';
import CustomerBooksTable from '~/components/customer/CustomerBooksTable';
import { useAlert } from '~/hooks/useAlert';

const CustomerBooksPage = () => {
  const [books, setBooks] = useState([]);
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(5);
  const [totalPages, setTotalPages] = useState(1);
  const [selectedBooks, setSelectedBooks] = useState([]);
  const [searchTitle, setSearchTitle] = useState('');
  const { addAlert } = useAlert();

  const getBooks = async () => {
    try {
      const { data, totalPages } = await fetchBooks(
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
    getBooks();
  };

  useEffect(() => {
    getBooks();
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
        <SearchTextField
          label="Szukaj książki"
          value={searchTitle}
          onChange={handleSearchChange}
          onSubmit={handleSearchSubmit}
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
      <CustomerBooksTable
        books={books}
        onToggleSelection={toggleBookSelection}
        selectedBooks={selectedBooks}
        refreshBooks={getBooks}
      />
      <Paginator total={totalPages} page={page} onChange={handlePageChange} />
    </Container>
  );
};

export default CustomerBooksPage;
