import { useState } from 'react';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import {
  Button,
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
import { FaCheck } from 'react-icons/fa';
import { IoClose } from 'react-icons/io5';
import { updateEmployer } from '~/api/employerApi';
import { useAlert } from '~/hooks/useAlert';
import EditEmployeeModal from './EditEmployeeModal';

const EmployersTable = ({
  employers,
  onDelete,
  onToggleSelection,
  selectedEmployers,
  onRegenerateAccess,
  refreshData,
}) => {
  const { addAlert } = useAlert();
  const [editModalOpen, setEditModalOpen] = useState(false);
  const [currentEmployee, setCurrentEmployee] = useState(null);

  const handleEditClick = employee => {
    setCurrentEmployee(employee);
    setEditModalOpen(true);
  };

  const handleModalClose = () => {
    setEditModalOpen(false);
    setCurrentEmployee(null);
  };

  const handleModalSubmit = async updatedData => {
    if (!currentEmployee) return;

    const response = await updateEmployer(currentEmployee.id, updatedData);

    if (response.success) {
      addAlert('Pracownik został zaktualizowany pomyślnie.', 'success');
      handleModalClose();
      refreshData();
    } else if (response.errors) {
      Object.entries(response.errors).forEach(([field, error]) => {
        addAlert(`Błąd w polu ${field}: ${error}`, 'error');
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
              <TableCell sx={{ color: 'white' }}>Imię i nazwisko</TableCell>
              <TableCell sx={{ color: 'white' }}>Email</TableCell>
              <TableCell sx={{ color: 'white' }}>Aktywny</TableCell>
              <TableCell sx={{ color: 'white' }}>Wybierz</TableCell>
              <TableCell sx={{ color: 'white' }}>Usuń</TableCell>
              <TableCell sx={{ color: 'white' }}>Regeneruj dostęp</TableCell>
              <TableCell sx={{ color: 'white' }}>Edytuj</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {employers.map(employer => (
              <TableRow key={employer.id}>
                <TableCell sx={{ color: 'white' }}>{employer.id}</TableCell>
                <TableCell sx={{ color: 'white' }}>
                  {employer.firstName + ' ' + employer.lastName}
                </TableCell>
                <TableCell sx={{ color: 'white' }}>{employer.email}</TableCell>
                <TableCell sx={{ color: 'white' }}>
                  {employer.active ? (
                    <FaCheck style={{ color: 'green', fontSize: '1.5rem' }} />
                  ) : (
                    <IoClose style={{ color: 'red', fontSize: '1.5rem' }} />
                  )}
                </TableCell>
                <TableCell>
                  <Checkbox
                    checked={selectedEmployers.includes(employer.id)}
                    onChange={() => onToggleSelection(employer.id)}
                    sx={{
                      color: 'white',
                      '&.Mui-checked': { color: 'custom.500' },
                    }}
                  />
                </TableCell>
                <TableCell>
                  <IconButton
                    onClick={() => onDelete(employer.id)}
                    sx={{
                      color: 'white',
                      '&:hover': { color: 'red' },
                    }}>
                    <DeleteIcon />
                  </IconButton>
                </TableCell>
                <TableCell>
                  {!employer.active && (
                    <Button
                      onClick={() => onRegenerateAccess(employer.id)}
                      variant="contained"
                      color="primary"
                      size="small"
                      sx={{ textTransform: 'none' }}>
                      Regeneruj
                    </Button>
                  )}
                </TableCell>
                <TableCell>
                  <IconButton
                    onClick={() => handleEditClick(employer)}
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
      {currentEmployee && (
        <EditEmployeeModal
          open={editModalOpen}
          onClose={handleModalClose}
          onSubmit={handleModalSubmit}
          initialFirstName={currentEmployee.firstName}
          initialLastName={currentEmployee.lastName}
        />
      )}
    </>
  );
};

EmployersTable.propTypes = {
  employers: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
      firstName: PropTypes.string.isRequired,
      lastName: PropTypes.string.isRequired,
      email: PropTypes.string.isRequired,
    })
  ).isRequired,
  onDelete: PropTypes.func.isRequired,
  onToggleSelection: PropTypes.func.isRequired,
  onRegenerateAccess: PropTypes.func,
  refreshData: PropTypes.func,
  selectedEmployers: PropTypes.arrayOf(
    PropTypes.oneOfType([PropTypes.string, PropTypes.number])
  ).isRequired,
};

export default EmployersTable;
