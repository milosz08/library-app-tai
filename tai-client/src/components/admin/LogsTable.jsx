import DeleteIcon from '@mui/icons-material/Delete';
import {
  Chip,
  IconButton,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from '@mui/material';
import PropTypes from 'prop-types';

const LogsTable = ({ logs, onDelete }) => {
  return (
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
            <TableCell sx={{ color: 'white' }}>Poziom</TableCell>
            <TableCell sx={{ color: 'white' }}>Wykonano</TableCell>
            <TableCell sx={{ color: 'white' }}>Wiadomość</TableCell>
            <TableCell sx={{ color: 'white' }}>Usuń</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {logs.map(log => (
            <TableRow key={log.id}>
              <TableCell sx={{ color: 'white' }}>{log.id}</TableCell>
              <TableCell>
                <Chip
                  label={log.level}
                  color={log.level === 'ERROR' ? 'error' : 'info'}
                  variant="outlined"
                />
              </TableCell>
              <TableCell sx={{ color: 'white' }}>{log.executedAt}</TableCell>
              <TableCell sx={{ color: 'white' }}>{log.message}</TableCell>
              <TableCell>
                <IconButton
                  onClick={() => onDelete(log)}
                  sx={{
                    color: 'white',
                    '&:hover': { color: 'red' },
                  }}>
                  <DeleteIcon />
                </IconButton>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

LogsTable.propTypes = {
  logs: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
      level: PropTypes.string.isRequired,
      executedAt: PropTypes.string.isRequired,
      message: PropTypes.string.isRequired,
    })
  ).isRequired,
  onDelete: PropTypes.func.isRequired,
};

export default LogsTable;
