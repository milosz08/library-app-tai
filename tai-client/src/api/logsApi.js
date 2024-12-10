import axiosInstance from './axiosConfig';

export const fetchLogs = (page, size) => {
  return axiosInstance
    .get('/logs', { params: { page, size } })
    .then(response => ({
      data: response.data.rows,
      totalResults: response.data.totalResults,
      totalPages: response.data.totalPages,
    }))
    .catch(error => {
      error.response?.data?.details || 'Nie udało się pobrać logów.';
    });
};

export const deleteLog = id => {
  return axiosInstance.delete('/logs/' + id);
};

export const deleteAllLogs = () => {
  return axiosInstance.delete('/logs');
};

export const deleteLastLogs = count => {
  return axiosInstance.delete('/logs/chunk/' + count);
};
