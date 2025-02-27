import axiosInstance from './axiosConfig';

export const register = form => {
  return axiosInstance
    .post('/v1/auth/register', form)
    .then(response => {
      if (response.status === 204) {
        return { success: true };
      }
      return { errors: { general: 'Rejestracja nie powiodła się.' } };
    })
    .catch(error => {
      const errors = error.response?.data || {};
      const fieldErrors = {
        firstName: errors.firstName,
        lastName: errors.lastName,
        city: errors.city,
        street: errors.street,
        buildingNumber: errors.buildingNumber,
        apartmentNumber: errors.apartmentNumber,
        email: errors.email,
        password: errors.password,
        confirmedPassword: errors.confirmedPassword,
      };

      Object.keys(fieldErrors).forEach(key => {
        if (!fieldErrors[key]) {
          delete fieldErrors[key];
        }
      });

      return { errors: fieldErrors };
    });
};

export const login = (email, password) => {
  return axiosInstance
    .post('/v1/auth/login', { email, password })
    .then(response => {
      if (response.status === 200) {
        return {
          status: response.status,
          ...response.data,
        };
      }
      return { status: response.status };
    })
    .catch(error => ({
      status: error.response?.status || 500,
      details: error.response?.data?.message || 'Nieprawidłowe dane logowania',
    }));
};

export const logout = () => {
  return axiosInstance.delete('/v1/auth/logout');
};

export const activateAccount = token => {
  return axiosInstance
    .patch('/v1/auth/activate/' + token)
    .then(response => {
      if (response.status >= 200 && response.status < 300) {
        return { success: true };
      } else {
        return { success: false, message: 'Aktywacja konta nie powiodła się.' };
      }
    })
    .catch(error => {
      return {
        success: false,
        message:
          error.response?.data?.message || 'Aktywacja konta nie powiodła się.',
      };
    });
};

export const sessionRevalidate = () => {
  return axiosInstance
    .patch('/v1/auth/session/revalidate')
    .then(response => {
      if (response.status === 200) {
        const { authenticated, role, roleName } = response.data;
        return {
          success: authenticated,
          role: role || null,
          roleName: roleName || null,
        };
      }
      return {
        success: false,
      };
    })
    .catch(() => {
      return {
        success: false,
      };
    });
};
