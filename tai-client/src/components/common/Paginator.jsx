import { Pagination } from '@mui/material';
import PropTypes from 'prop-types';

const Paginator = ({ total, page, onChange }) => (
  <Pagination
    count={total}
    page={page}
    onChange={onChange}
    sx={{
      mt: 2,
      display: 'flex',
      justifyContent: 'center',
      '& .MuiPaginationItem-root': { color: 'white' },
      '& .Mui-selected': { backgroundColor: 'custom.500' },
    }}
  />
);

Paginator.propTypes = {
  total: PropTypes.number.isRequired,
  page: PropTypes.number.isRequired,
  onChange: PropTypes.func.isRequired,
};

export default Paginator;
