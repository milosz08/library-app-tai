import axiosInstance from './axiosConfig';

export const details = () => {
  return axiosInstance
    .get('/v1/@me')
    .then(response => response.data)
    .catch(() => {
      return { error: 'Nie udało się pobrać danych użytkownika.' };
    });
};

export const deleteAccount = () => {
  return axiosInstance
    .delete('/v1/@me')
    .then(response => response)
    .catch(error => {
      const errorMessage =
        error.response?.data?.details ||
        'Wystąpił błąd podczas usuwania konta.';
      return { message: errorMessage };
    });
};

export const updatePersonalDetails = personalDetails => {
  return axiosInstance
    .patch('/v1/@me', personalDetails)
    .then(response => response.data)
    .catch(error => {
      const errors = error.response?.data || {};
      const fieldErrors = {
        firstName: errors.firstName,
        lastName: errors.lastName,
      };

      Object.keys(fieldErrors).forEach(key => {
        if (!fieldErrors[key]) {
          delete fieldErrors[key];
        }
      });

      return { errors: fieldErrors };
    });
};

export const updateAddressDetails = addressDetails => {
  return axiosInstance
    .patch('/v1/@me/address', addressDetails)
    .then(response => response.data)
    .catch(error => {
      const errors = error.response?.data || {};
      const fieldErrors = {
        city: errors.city,
        street: errors.street,
        buildingNumber: errors.buildingNumber,
        apartmentNumber: errors.apartmentNumber,
      };

      Object.keys(fieldErrors).forEach(key => {
        if (!fieldErrors[key]) {
          delete fieldErrors[key];
        }
      });

      return { errors: fieldErrors };
    });
};
