import axiosInstance from './axiosConfig';

export const register = form => {
  return axiosInstance
    .post('/auth/register', form)
    .then(response => {
      return { token: response.data.token };
    })
    .catch(error => {
      const errorMessage =
        error.response?.data?.details ||
        error.response?.data?.firstName ||
        error.response?.data?.lastName ||
        error.response?.data?.city ||
        error.response?.data?.street ||
        error.response?.data?.buildingNumber ||
        error.response?.data?.apartmentNumber ||
        error.response?.data?.email ||
        error.response?.data?.password ||
        error.response?.data?.confirmedPassword ||
        'Rejestracja nie powiodła się. Spróbuj ponownie.';

      return { error: errorMessage };
    });
};

export const login = (email, password) => {
  return axiosInstance
    .post('/auth/login', { email, password })
    .then(response => {
      if (response.status === 200) {
        return {
          status: response.status,
          token: response.data.token,
        };
      } else if (response.status === 204) {
        return { status: response.status };
      }
      return { status: response.status };
    })
    .catch(error => ({
      status: error.response?.status || 500,
      details: error.response?.data?.message || 'Nieprawidłowe dane logowania',
    }));
};

export const logout = () => {
  return axiosInstance
    .delete('/auth/logout')
    .then(response => response.status === 200)
    .catch(() => false);
};

export const activateAccount = token => {
  return axiosInstance
    .patch('/auth/activate/' + token)
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
