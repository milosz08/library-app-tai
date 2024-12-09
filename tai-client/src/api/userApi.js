import axiosInstance from './axiosConfig';

export const details = () => {
  return axiosInstance
    .get('/@me')
    .then(response => response.data)
    .catch();
};

export const updatePersonalDetails = async personalDetails => {
  try {
    const response = await axiosInstance.patch('/@me', personalDetails);
    return response.data;
  } catch (error) {
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
  }
};

export const updateAddressDetails = async addressDetails => {
  try {
    const response = await axiosInstance.patch('/@me/address', addressDetails);
    return response.data;
  } catch (error) {
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
  }
};
