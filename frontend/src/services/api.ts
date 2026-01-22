import axios from 'axios';
import type { AxiosInstance } from 'axios';

/**
 * Instância do Axios configurada para comunicação com o backend.
 */
const api: AxiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para tratamento de erros
api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('Erro na requisição:', error);
    return Promise.reject(error);
  }
);

export default api;
