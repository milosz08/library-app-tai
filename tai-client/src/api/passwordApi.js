import axiosInstance from './axiosConfig';

export const resetPassword = email => {
  return axiosInstance
    .patch('/forgot/password/request', { email })
    .then(response => ({ success: true, message: response.data.token }))
    .catch(error => {
      const errorMessage =
        error.response?.data?.message || 'Wystąpił błąd. Spróbuj ponownie.';
      return { success: false, message: errorMessage };
    });
};

export const renewPassword = (token, form) => {
  return axiosInstance
    .patch('/forgot/password/renew/' + token, {
      newPassword: form.newPassword,
      confirmedNewPassword: form.confirmedNewPassword,
    })
    .then(() => ({ success: true }))
    .catch(error => {
      const errorMessage =
        error.response?.data?.newPassword ||
        error.response?.data?.details ||
        'Wystąpił błąd. Spróbuj ponownie.';
      return { success: false, message: errorMessage };
    });
};
