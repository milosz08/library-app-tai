import axiosInstance from './axiosConfig';

export const fetchBooks = (page, size, title) => {
  return axiosInstance
    .get('/v1/book', { params: { page, size, title } })
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
    .get('/v1/book/' + id)
    .then(response => ({ success: true, data: response.data }))
    .catch(error => {
      return {
        success: false,
        message:
          error.response?.data?.details || 'Nie udało się pobrać książki',
      };
    });
};

export const addBook = bookData => {
  return axiosInstance
    .post('/v1/book', bookData)
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
        authors: parseAuthorErrors(errors),
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
    .patch('/v1/book/' + id, form)
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
        authors: parseAuthorErrors(errors),
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

const parseAuthorErrors = errors => {
  const authorErrors = {};
  for (const key in errors) {
    if (key.startsWith('authors[')) {
      const match = key.match(/authors\[(\d+)\]\.(.+)/);
      if (match) {
        const index = match[1];
        const field = match[2];
        authorErrors[index] = authorErrors[index] || {};
        authorErrors[index][field] = errors[key];
      }
    }
  }
  return Object.keys(authorErrors).length ? authorErrors : null;
};

export const deleteBook = async id => {
  try {
    await axiosInstance.delete(`/v1/book/${id}`);
    return { message: 'Pomyślnie usunięto książkę.', type: 'success' };
  } catch (e) {
    return {
      message:
        e.response.data.details || 'Wystąpił błąd podczas usuwania książki.',
      type: 'error',
    };
  }
};

export const deleteBooks = async callback => {
  try {
    const { data } = await callback(axiosInstance);
    return {
      message: `Liczba usuniętych książek: ${data.count}`,
      type: 'success',
    };
  } catch (e) {
    return {
      message:
        e.response.data.details || 'Wystąpił błąd podczas usuwania książki.',
      type: 'error',
    };
  }
};
