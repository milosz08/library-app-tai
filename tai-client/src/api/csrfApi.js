import axios from 'axios';

const apiUrl = import.meta.env.VITE_SERVER_URL;

export const fetchCsrfToken = () => {
  return axios
    .get(apiUrl + '/csrf/token', { withCredentials: true })
    .then(response => response.data.token);
};
