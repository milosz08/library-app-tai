import { useEffect, useState } from 'react';
import { Box, Button, Container, MenuItem, Select } from '@mui/material';
import {
  deleteAllEmployers,
  deleteEmployer,
  deleteSelectedEmployers,
  fetchEmployers,
  regenerateFirstAccess,
} from '~/api/employerApi';
import EmployersTable from '~/components/admin/EmployersTable';
import ConfirmationModal from '~/components/common/ConfirmationModal';
import Paginator from '~/components/common/Paginator';
import SearchTextField from '~/components/common/SearchTextField';
import { useAlert } from '~/hooks/useAlert';
import { useLoader } from '~/hooks/useLoader';

const EmployersPage = () => {
  const [employers, setEmployers] = useState([]);
  const { setIsLoading } = useLoader();
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(5);
  const [totalPages, setTotalPages] = useState(1);
  const [selectedEmployers, setSelectedEmployers] = useState([]);
  const [isDeleteAllModalOpen, setDeleteAllModalOpen] = useState(false);
  const [deleteEmployerId, setDeleteEmployerId] = useState(null);
  const [isDeleteSelectedModalOpen, setDeleteSelectedModalOpen] =
    useState(false);
  const { addAlert } = useAlert();
  const [searchEmployer, setSearchEmployer] = useState('');

  const loadEmployers = async () => {
    try {
      const { data, totalPages } = await fetchEmployers(
        page,
        pageSize,
        searchEmployer
      );
      setEmployers(data);
      setTotalPages(totalPages);
    } catch (error) {
      addAlert('Nie udało się załadować pracowników.', 'error');
    }
  };

  const handleDelete = async id => {
    try {
      await deleteEmployer(id);
      addAlert('Pracownik został usunięty.', 'success');
      loadEmployers();
    } catch (error) {
      addAlert('Wystąpił błąd podczas usuwania pracownika.', 'error');
    }
  };

  const handleDeleteAll = async () => {
    try {
      await deleteAllEmployers();
      addAlert('Wszyscy pracownicy zostali usunięci.', 'success');
      loadEmployers();
    } catch (error) {
      addAlert('Wystąpił błąd podczas usuwania pracowników.', 'error');
    }
  };

  const handleDeleteSelected = async () => {
    if (selectedEmployers.length === 0) {
      addAlert('Nie wybrano żadnych pracowników do usunięcia.', 'warning');
      return;
    }

    try {
      await deleteSelectedEmployers(selectedEmployers);
      addAlert('Wybrani pracownicy zostali usunięci.', 'success');
      setSelectedEmployers([]);
      loadEmployers();
    } catch (error) {
      addAlert(
        'Wystąpił błąd podczas usuwania wybranych pracowników.',
        'error'
      );
    }
  };
  const handleRegenerateAccess = async id => {
    setIsLoading(true);

    const response = await regenerateFirstAccess(id);

    if (response.success) {
      addAlert(
        'Nowy link i hasło zostały wygenerowane oraz wysłane na adres email powiązany z wybranym kontem pracownika.',
        'info'
      );
    } else {
      addAlert(response.message, 'error');
    }

    setIsLoading(false);
  };

  const handlePageChange = (_, value) => {
    setPage(value);
  };

  const handlePageSizeChange = event => {
    setPageSize(event.target.value);
    setPage(1);
  };

  const toggleEmployerSelection = id => {
    setSelectedEmployers(prev =>
      prev.includes(id)
        ? prev.filter(selectedId => selectedId !== id)
        : [...prev, id]
    );
  };

  const handleSearchChange = event => {
    setSearchEmployer(event.target.value);
  };

  const handleSearchSubmit = () => {
    setPage(1);
    loadEmployers();
  };

  const openDeleteSelectedModal = () => {
    setDeleteSelectedModalOpen(true);
  };

  const closeDeleteSelectedModal = () => {
    setDeleteSelectedModalOpen(false);
  };

  const openDeleteEmployerModal = id => {
    setDeleteEmployerId(id);
  };

  const closeDeleteEmployerModal = () => {
    setDeleteEmployerId(null);
  };

  const openDeleteAllModal = () => {
    setDeleteAllModalOpen(true);
  };

  const closeDeleteAllModal = () => {
    setDeleteAllModalOpen(false);
  };

  useEffect(() => {
    loadEmployers();
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
          label="Szukaj email"
          value={searchEmployer}
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
        <Button
          variant="contained"
          color="warning"
          onClick={openDeleteSelectedModal}
          disabled={selectedEmployers.length === 0}
          sx={{
            flex: '1 1 auto',
            textTransform: 'none',
          }}>
          Usuń wybranych pracowników
        </Button>
        <Button
          variant="contained"
          color="error"
          onClick={openDeleteAllModal}
          sx={{
            flex: '1 1 auto',
            textTransform: 'none',
          }}>
          Usuń wszystkich pracowników
        </Button>
      </Box>
      <EmployersTable
        employers={employers}
        onDelete={openDeleteEmployerModal}
        onToggleSelection={toggleEmployerSelection}
        selectedEmployers={selectedEmployers}
        onRegenerateAccess={handleRegenerateAccess}
        refreshData={loadEmployers}
      />
      <Paginator total={totalPages} page={page} onChange={handlePageChange} />
      {isDeleteSelectedModalOpen && (
        <ConfirmationModal
          open={isDeleteSelectedModalOpen}
          title="Potwierdzenie usunięcia"
          description="Czy na pewno chcesz usunąć wybranych pracowników?"
          onConfirm={() => {
            handleDeleteSelected();
            closeDeleteSelectedModal();
          }}
          onCancel={closeDeleteSelectedModal}
        />
      )}
      {deleteEmployerId && (
        <ConfirmationModal
          open={!!deleteEmployerId}
          title="Potwierdzenie usunięcia"
          description="Czy na pewno chcesz usunąć tego pracownika?"
          onConfirm={() => {
            handleDelete(deleteEmployerId);
            closeDeleteEmployerModal();
          }}
          onCancel={closeDeleteEmployerModal}
        />
      )}
      {isDeleteAllModalOpen && (
        <ConfirmationModal
          open={isDeleteAllModalOpen}
          title="Potwierdzenie usunięcia"
          description="Czy na pewno chcesz usunąć wszystkich pracowników?"
          onConfirm={() => {
            handleDeleteAll();
            closeDeleteAllModal();
          }}
          onCancel={closeDeleteAllModal}
        />
      )}
    </Container>
  );
};

export default EmployersPage;
