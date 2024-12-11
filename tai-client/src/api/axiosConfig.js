import axios from 'axios';
import { executeLogout } from '~/utils/authManager';
import { fetchCsrfToken } from './csrfApi';

const apiUrl = import.meta.env.VITE_SERVER_URL;

let csrfTokenCache = null;

const axiosInstance = axios.create({
  baseURL: apiUrl,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
});

axiosInstance.interceptors.request.use(
  async config => {
    config.headers['Accept-Language'] = 'pl';
    if (['post', 'put', 'patch', 'delete'].includes(config.method)) {
      if (!config.headers['X-CSRF-Token']) {
        csrfTokenCache = await fetchCsrfToken();
        config.headers['X-CSRF-Token'] = csrfTokenCache;
      }
    }
    return config;
  },
  error => Promise.reject(error)
);

axiosInstance.interceptors.response.use(
  response => response,
  async error => {
    const status = error.response?.status;

    if (status === 401) {
      await executeLogout();
    }

    return Promise.reject(error);
  }
);

export default axiosInstance;
