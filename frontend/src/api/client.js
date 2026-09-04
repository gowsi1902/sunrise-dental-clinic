import axios from 'axios';

const api = axios.create({
  baseURL: '/sunrise',
  withCredentials: true
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401 && !window.location.pathname.startsWith('/login')) {
      localStorage.removeItem('sunrise-user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;
