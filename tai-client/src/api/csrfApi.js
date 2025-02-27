import axios from 'axios';

export const fetchCsrfToken = () => {
  return axios
    .get('/api/v1/csrf/token', { withCredentials: true })
    .then(response => response.data.token);
};
