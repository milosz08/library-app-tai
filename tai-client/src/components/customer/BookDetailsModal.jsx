import {
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Typography,
} from '@mui/material';
import PropTypes from 'prop-types';

const BookDetailsModal = ({ open, onClose, bookDetails }) => {
  return (
    <Dialog
      open={open}
      onClose={onClose}
      PaperProps={{
        sx: {
          position: 'absolute',
          top: 20,
          margin: 0,
          backgroundColor: 'custom.700',
          color: 'custom.50',
          minWidth: '400px',
        },
      }}>
      <DialogTitle
        sx={{
          backgroundColor: 'custom.700',
          color: 'custom.50',
          fontWeight: 'bold',
        }}>
        Szczegóły książki
      </DialogTitle>
      <DialogContent
        sx={{
          display: 'flex',
          flexDirection: 'column',
          gap: 2,
          justifyContent: 'center',
        }}>
        <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
          <Typography variant="body1">
            <strong>Tytuł:</strong> {bookDetails.title}
          </Typography>
          <Typography variant="body1">
            <strong>Rok wydania:</strong> {bookDetails.year}
          </Typography>
          <Typography variant="body1">
            <strong>Wydawnictwo:</strong> {bookDetails.publisher}
          </Typography>
          <Typography variant="body1">
            <strong>Miejscowość:</strong> {bookDetails.city}
          </Typography>
          <Typography variant="body1">
            <strong>Liczba kopii:</strong> {bookDetails.copies}
          </Typography>
          {bookDetails.authors && (
            <Box>
              <Typography variant="body1">
                <strong>Autorzy:</strong>
              </Typography>
              {bookDetails.authors.map((author, index) => (
                <Typography key={index} variant="body2" sx={{ ml: 2 }}>
                  {author.firstName} {author.lastName}
                </Typography>
              ))}
            </Box>
          )}
        </Box>
      </DialogContent>
      <DialogActions
        sx={{
          backgroundColor: 'custom.700',
        }}>
        <Button
          onClick={onClose}
          sx={{
            color: 'white',
            backgroundColor: '#d32f2f',
            '&:hover': {
              backgroundColor: '#9e2b2b',
            },
          }}>
          Zamknij
        </Button>
      </DialogActions>
    </Dialog>
  );
};

BookDetailsModal.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  bookDetails: PropTypes.shape({
    title: PropTypes.string,
    year: PropTypes.number,
    publisher: PropTypes.string,
    city: PropTypes.string,
    copies: PropTypes.number,
    authors: PropTypes.arrayOf(
      PropTypes.shape({
        firstName: PropTypes.string,
        lastName: PropTypes.string,
      })
    ),
  }).isRequired,
};

export default BookDetailsModal;
