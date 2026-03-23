import axios from 'axios';

// Tworzymy bazową instancję - nie musimy już wpisywać pełnego URL za każdym razem!
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
});

// "Przechwytywacz" (Interceptor) zapytań
api.interceptors.request.use(
  (config) => {
    // Sprawdzamy, czy mamy zapisany token w pamięci przeglądarki (localStorage)
    const token = localStorage.getItem('token');
    if (token) {
      // Jeśli tak, doklejamy magiczny nagłówek (nasza Opaska VIP)
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default api;