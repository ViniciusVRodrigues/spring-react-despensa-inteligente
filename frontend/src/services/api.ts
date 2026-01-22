import axios from 'axios';
import type { AxiosInstance } from 'axios';

/**
 * URL base da API, configurável via variável de ambiente.
 * Padrão: http://localhost:8081/api
 */
const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8081/api';

/**
 * Instância do Axios configurada para comunicação com o backend.
 */
const api: AxiosInstance = axios.create({
  baseURL: API_URL,
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
