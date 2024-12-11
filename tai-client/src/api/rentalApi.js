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

export const rentBook = (id, count) => {
  return axiosInstance
    .patch('/rental/loan', {
      bookId: id,
      count,
    })
    .then(response => {
      if (response.status === 204) {
        return {
          success: true,
        };
      } else {
        return {
          success: false,
          message: 'Nie udało się wypożyczyć książki',
        };
      }
    })
    .catch(error => {
      const errorMessage =
        error.response?.data?.details ||
        'Nie udało się wypożyczyć książki o ID: ' + id;
      return { success: false, message: errorMessage };
    });
};
