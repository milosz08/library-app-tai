import axiosInstance from './axiosConfig';

export const fetchBooks = (page, size, title) => {
  return axiosInstance
    .get('/book', { params: { page, size, title } })
    .then(response => ({
      data: response.data.rows,
      totalResults: response.data.totalResults,
      totalPages: response.data.totalPages,
    }))
    .catch(error => {
      return (
        error.response?.data?.details || 'Nie udało się pobrać listy książek.'
      );
    });
};

export const fetchBookDetails = id => {
  return axiosInstance
    .get('/book/' + id)
    .then(response => response.data)
    .catch(error => {
      return (
        error.response?.data?.details ||
        'Nie udało się pobrać książki o id: ' + id
      );
    });
};

export const addBook = bookData => {
  return axiosInstance
    .post('/book', bookData)
    .then(response => {
      if (response.status === 204) {
        return { success: true };
      }
      return { errors: { general: 'Dodanie książki nie powiodło się.' } };
    })
    .catch(error => {
      const errors = error.response?.data || {};
      const fieldErrors = {
        title: errors.title,
        year: errors.year,
        publisher: errors.publisher,
        city: errors.city,
        copies: errors.copies,
        authors: errors.authors,
        details: errors.details,
      };

      Object.keys(fieldErrors).forEach(key => {
        if (!fieldErrors[key]) {
          delete fieldErrors[key];
        }
      });

      return {
        errors: fieldErrors,
        details: errors.details || null,
      };
    });
};

export const updateBook = (id, form) => {
  return axiosInstance
    .patch('/book/' + id, form)
    .then(() => {
      return { success: true };
    })
    .catch(error => {
      const errors = error.response?.data || {};
      const fieldErrors = {
        title: errors.title,
        year: errors.year,
        publisher: errors.publisher,
        city: errors.city,
        copies: errors.copies,
        authors: errors.authors,
        details: errors.details,
      };

      Object.keys(fieldErrors).forEach(key => {
        if (!fieldErrors[key]) {
          delete fieldErrors[key];
        }
      });

      return { errors: fieldErrors };
    });
};

export const deleteBook = id => {
  return axiosInstance.delete('/book/' + id);
};

export const deleteAllBooks = () => {
  return axiosInstance.delete('/book');
};

export const deleteSelectedBooks = ids => {
  return axiosInstance.delete('/book/selected', {
    data: { ids },
  });
};
