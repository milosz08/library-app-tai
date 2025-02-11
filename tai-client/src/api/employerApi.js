import axiosInstance from './axiosConfig';

export const fetchEmployers = (page, size, email) => {
  return axiosInstance
    .get('/employer', { params: { page, size, email } })
    .then(response => ({
      data: response.data.rows,
      totalResults: response.data.totalResults,
      totalPages: response.data.totalPages,
    }))
    .catch(error => {
      return (
        error.response?.data?.details ||
        'Nie udało się pobrać listy pracowników.'
      );
    });
};

export const createEmployer = form => {
  return axiosInstance
    .post('/employer', form)
    .then(response => {
      if (response.status === 204) {
        return { success: true };
      }
      return { errors: { general: 'Tworzenie pracownika nie powiodło się.' } };
    })
    .catch(error => {
      const errors = error.response?.data || {};
      const fieldErrors = {
        firstName: errors.firstName,
        lastName: errors.lastName,
        email: errors.email,
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

export const updateEmployer = (id, form) => {
  return axiosInstance
    .patch('/employer/' + id, form)
    .then(() => {
      return { success: true };
    })
    .catch(error => {
      const errors = error.response?.data || {};
      const fieldErrors = {
        firstName: errors.firstName,
        lastName: errors.lastName,
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

export const deleteEmployer = id => {
  return axiosInstance.delete('/employer/' + id);
};

export const deleteAllEmployers = () => {
  return axiosInstance.delete('/employer');
};

export const deleteSelectedEmployers = ids => {
  return axiosInstance.delete('/employer/selected', {
    data: { ids },
  });
};

export const changePasswordOnFirstAccess = (token, form) => {
  return axiosInstance
    .patch('/employer/first/access/' + token + '/password', form)
    .then(response => {
      if (response.status === 204) {
        return { success: true, message: 'Hasło zostało pomyślnie zmienione.' };
      }
      return { errors: { general: 'Zmiana hasła nie powiodła się.' } };
    })
    .catch(error => {
      const errors = error.response?.data || {};
      const fieldErrors = {
        temporaryPassword: errors.temporaryPassword,
        newPassword: errors.newPassword,
        confirmedNewPassword: errors.confirmedNewPassword,
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

export const regenerateFirstAccess = id => {
  return axiosInstance
    .patch('/employer/' + id + '/first/access/regenerate')
    .then(response => {
      if (response.status === 204) {
        return { success: true };
      }
      return { success: false, message: 'Nie udało się wygenerować danych.' };
    })
    .catch(error => {
      const message =
        error.response?.data?.message || 'Wystąpił błąd podczas generowania.';
      return { success: false, message };
    });
};
