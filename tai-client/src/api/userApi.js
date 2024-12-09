import axiosInstance from './axiosConfig';

export const details = () => {
  return axiosInstance
    .get('/@me')
    .then(response => response.data)
    .catch(() => {
      return { error: 'Nie udało się pobrać danych użytkownika.' };
    });
};

export const deleteAccount = () => {
  return axiosInstance.delete('/@me').then(response => response.status);
};

export const updatePersonalDetails = personalDetails => {
  return axiosInstance
    .patch('/@me', personalDetails)
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
    .patch('/@me/address', addressDetails)
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
