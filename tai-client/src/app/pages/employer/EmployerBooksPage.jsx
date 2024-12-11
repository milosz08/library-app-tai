import { useEffect, useState } from 'react';
import {
  Box,
  Button,
  Container,
  MenuItem,
  Pagination,
  Select,
  TextField,
} from '@mui/material';
import {
  deleteAllBooks,
  deleteBook,
  deleteSelectedBooks,
  fetchBooks,
} from '~/api/bookApi';
import ConfirmationModal from '~/components/common/ConfirmationModal';
import EmployerBooksTable from '~/components/employer/EmployerBooksTable';
import { useAlert } from '~/hooks/useAlert';

const EmployerBooksPage = () => {
  const [books, setBooks] = useState([]);
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(5);
  const [totalPages, setTotalPages] = useState(1);
  const [selectedBooks, setSelectedBooks] = useState([]);
  const [isDeleteAllModalOpen, setDeleteAllModalOpen] = useState(false);
  const [deleteBookId, setDeleteBookId] = useState(null);
  const [isDeleteSelectedModalOpen, setDeleteSelectedModalOpen] =
    useState(false);
  const [searchTitle, setSearchTitle] = useState('');
  const { addAlert } = useAlert();

  const loadBooks = async () => {
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

  const handleDelete = async id => {
    try {
      await deleteBook(id);
      addAlert('Książka została usunięta.', 'success');
      loadBooks();
    } catch (error) {
      addAlert('Wystąpił błąd podczas usuwania książki.', 'error');
    }
  };

  const handleDeleteAll = async () => {
    try {
      await deleteAllBooks();
      addAlert('Wszystkie książki zostały usunięte.', 'success');
      loadBooks();
    } catch (error) {
      addAlert('Wystąpił błąd podczas usuwania książek.', 'error');
    }
  };

  const handleDeleteSelected = async () => {
    if (selectedBooks.length === 0) {
      addAlert('Nie wybrano żadnych książek do usunięcia.', 'warning');
      return;
    }

    try {
      await deleteSelectedBooks(selectedBooks);
      addAlert('Wybrane książki zostały usunięte.', 'success');
      setSelectedBooks([]);
      loadBooks();
    } catch (error) {
      addAlert('Wystąpił błąd podczas usuwania wybranych książek.', 'error');
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
    loadBooks();
  };

  useEffect(() => {
    loadBooks();
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
        <Button
          variant="contained"
          color="warning"
          onClick={() => setDeleteSelectedModalOpen(true)}
          disabled={selectedBooks.length === 0}
          sx={{
            flex: '1 1 auto',
            textTransform: 'none',
          }}>
          Usuń wybrane książki
        </Button>
        <Button
          variant="contained"
          color="error"
          onClick={() => setDeleteAllModalOpen(true)}
          sx={{
            flex: '1 1 auto',
            textTransform: 'none',
          }}>
          Usuń wszystkie książki
        </Button>
      </Box>
      <EmployerBooksTable
        books={books}
        onDelete={id => setDeleteBookId(id)}
        onToggleSelection={toggleBookSelection}
        selectedBooks={selectedBooks}
        refreshData={loadBooks}
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
      {isDeleteSelectedModalOpen && (
        <ConfirmationModal
          open={isDeleteSelectedModalOpen}
          title="Potwierdzenie usunięcia"
          description="Czy na pewno chcesz usunąć wybrane książki?"
          onConfirm={() => {
            handleDeleteSelected();
            setDeleteSelectedModalOpen(false);
          }}
          onCancel={() => setDeleteSelectedModalOpen(false)}
        />
      )}
      {deleteBookId && (
        <ConfirmationModal
          open={!!deleteBookId}
          title="Potwierdzenie usunięcia"
          description="Czy na pewno chcesz usunąć tę książkę?"
          onConfirm={() => {
            handleDelete(deleteBookId);
            setDeleteBookId(null);
          }}
          onCancel={() => setDeleteBookId(null)}
        />
      )}
      {isDeleteAllModalOpen && (
        <ConfirmationModal
          open={isDeleteAllModalOpen}
          title="Potwierdzenie usunięcia"
          description="Czy na pewno chcesz usunąć wszystkie książki?"
          onConfirm={() => {
            handleDeleteAll();
            setDeleteAllModalOpen(false);
          }}
          onCancel={() => setDeleteAllModalOpen(false)}
        />
      )}
    </Container>
  );
};

export default EmployerBooksPage;
