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
  deleteAllLogs,
  deleteLastLogs,
  deleteLog,
  fetchLogs,
} from '~/api/logsApi';
import LogsTable from '~/components/admin/LogsTable';
import ConfirmationModal from '~/components/common/ConfirmationModal';
import { useAlert } from '~/hooks/useAlert';

const LogsPage = () => {
  const [logs, setLogs] = useState([]);
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(5);
  const [totalPages, setTotalPages] = useState(1);
  const [totalResults, setTotalResults] = useState(0);
  const [selectedLog, setSelectedLog] = useState(null);
  const [isModalOpen, setModalOpen] = useState(false);
  const [isDeleteAllModalOpen, setDeleteAllModalOpen] = useState(false);
  const [isDeleteLastModalOpen, setDeleteLastModalOpen] = useState(false);
  const [lastLogsCount, setLastLogsCount] = useState(0);
  const { addAlert } = useAlert();

  const loadLogs = async () => {
    try {
      const { data, totalPages, totalResults } = await fetchLogs(
        page,
        pageSize
      );
      setLogs(data);
      setTotalResults(totalResults);
      setTotalPages(totalPages);
    } catch (error) {
      addAlert('Nie udało się załadować logów.', 'error');
    }
  };

  const handleDelete = async id => {
    try {
      await deleteLog(id);
      addAlert('Log został usunięty.', 'success');
      loadLogs();
    } catch (error) {
      addAlert('Wystąpił błąd podczas usuwania loga.', 'error');
    }
  };

  const handleDeleteAll = async () => {
    try {
      await deleteAllLogs();
      addAlert('Wszystkie logi zostały usunięte.', 'success');
      loadLogs();
    } catch (error) {
      addAlert('Wystąpił błąd podczas usuwania wszystkich logów.', 'error');
    }
  };

  const handleDeleteLastLogs = async () => {
    if (lastLogsCount <= 0 || lastLogsCount > totalResults) {
      addAlert('Podaj poprawną liczbę logów do usunięcia.', 'warning');
      return;
    }
    try {
      await deleteLastLogs(lastLogsCount);
      addAlert(`Usunięto ostatnich ${lastLogsCount} logów.`, 'success');
      setLastLogsCount(0);
      loadLogs();
    } catch (error) {
      addAlert('Wystąpił błąd podczas usuwania ostatnich logów.', 'error');
    }
  };

  const handlePageChange = (_, value) => {
    setPage(value);
  };

  const handlePageSizeChange = event => {
    setPageSize(event.target.value);
    setPage(1);
  };

  const openDeleteModal = log => {
    setSelectedLog(log);
    setModalOpen(true);
  };

  const closeDeleteModal = () => {
    setSelectedLog(null);
    setModalOpen(false);
  };

  const confirmDelete = () => {
    if (selectedLog) {
      handleDelete(selectedLog.id);
      closeDeleteModal();
    }
  };

  const openDeleteAllModal = () => {
    setDeleteAllModalOpen(true);
  };

  const closeDeleteAllModal = () => {
    setDeleteAllModalOpen(false);
  };

  const openDeleteLastLogsModal = () => {
    setDeleteLastModalOpen(true);
  };

  const closeDeleteLastLogsModal = () => {
    setDeleteLastModalOpen(false);
  };

  useEffect(() => {
    loadLogs();
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
        }}>
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
        <Box
          sx={{
            display: 'flex',
            alignItems: 'center',
            flex: '1 1 auto',
            gap: 1,
            paddingY: 2,
          }}>
          <TextField
            type="number"
            value={lastLogsCount}
            onChange={e =>
              setLastLogsCount(
                Math.min(Math.max(Number(e.target.value), 0), totalResults)
              )
            }
            label="Zakres logów"
            variant="outlined"
            size="small"
            slotProps={{
              input: {
                sx: {
                  bgcolor: 'custom.700',
                  color: 'custom.50',
                },
              },
              inputLabel: {
                sx: {
                  color: 'custom.50',
                },
              },
            }}
            sx={{
              flex: '1 1 auto',
            }}
          />
          <Button
            variant="contained"
            color="warning"
            onClick={openDeleteLastLogsModal}
            sx={{
              flex: '1 1 auto',
              textTransform: 'none',
            }}>
            Usuń ostatnie logi
          </Button>
        </Box>
        <Button
          variant="contained"
          color="error"
          onClick={openDeleteAllModal}
          sx={{
            flex: '1 1 auto',
            textTransform: 'none',
          }}>
          Usuń wszystkie logi
        </Button>
      </Box>
      <LogsTable logs={logs} onDelete={openDeleteModal} />
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
      {isModalOpen && (
        <ConfirmationModal
          open={isModalOpen}
          title="Potwierdzenie usunięcia"
          description={
            'Czy na pewno chcesz usunąć log o ID ' + selectedLog?.id + ' ?'
          }
          onConfirm={confirmDelete}
          onCancel={closeDeleteModal}
        />
      )}
      {isDeleteAllModalOpen && (
        <ConfirmationModal
          open={isDeleteAllModalOpen}
          title="Potwierdzenie usunięcia"
          description="Czy na pewno chcesz usunąć wszystkie logi?"
          onConfirm={() => {
            handleDeleteAll();
            closeDeleteAllModal();
          }}
          onCancel={closeDeleteAllModal}
        />
      )}
      {isDeleteLastModalOpen && (
        <ConfirmationModal
          open={isDeleteLastModalOpen}
          title="Potwierdzenie usunięcia"
          description={
            'Czy na pewno chcesz usunąć ostatnie ' + lastLogsCount + ' logów?'
          }
          onConfirm={() => {
            handleDeleteLastLogs();
            closeDeleteLastLogsModal();
          }}
          onCancel={closeDeleteLastLogsModal}
        />
      )}
    </Container>
  );
};

export default LogsPage;
