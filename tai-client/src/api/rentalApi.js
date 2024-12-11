import axiosInstance from './axiosConfig';

export const fetchRentalBooks = (page, size, title) => {
  return axiosInstance
    .get('/rental/rented', { params: { page, size, title } })
    .then(response => ({
      data: response.data.rows,
      totalResults: response.data.totalResults,
      totalPages: response.data.totalPages,
    }))
    .catch(error => {
      return (
        error.response?.data?.details ||
        'Nie udało się pobrać listy wypożyczonych książek.'
      );
    });
};

export const fetchRentedDetails = id => {
  return axiosInstance
    .get('/rental/rented/' + id)
    .then(response => response.data)
    .catch(error => {
      return (
        error.response?.data?.details ||
        'Nie udało się pobrać informacji o wypożyczeniu książki: ' + id
      );
    });
};

export const rentBook = (bookId, count) => {
  return axiosInstance
    .patch('/rental/loan', {
      bookId,
      count,
    })
    .then(response => response.status)
    .catch(error => {
      return (
        error.response?.data?.details ||
        'Nie udało się zaktualizować danych wypożyczenia.'
      );
    });
};
