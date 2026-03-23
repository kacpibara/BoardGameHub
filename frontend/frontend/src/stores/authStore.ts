import { defineStore } from 'pinia';
import api from '../api/axios';
import router from '../router';

export const useAuthStore = defineStore('auth', {
  // 1. STATE (Stan) - tutaj trzymamy dane
  state: () => ({
    token: localStorage.getItem('token') || null,
  }),

  // 2. GETTERS - wygodne funkcje sprawdzające stan
  getters: {
    isAuthenticated: (state) => !!state.token, // Jeśli jest token, zwraca true
  },

  // 3. ACTIONS - funkcje zmieniające stan (wykonywanie logowania)
  actions: {
    async login(email: string, password: string) {
      try {
        // Wysyłamy POST na /api/auth/authenticate
        const response = await api.post('/auth/authenticate', { email, password });
        
        // Zapisujemy token do zmiennej w Pinii
        this.token = response.data.token;
        
        // Zapisujemy token na twardo w przeglądarce (żeby nie znikał po odświeżeniu strony/F5)
        localStorage.setItem('token', response.data.token);
        
        // Po udanym logowaniu przenosimy użytkownika na stronę główną!
        router.push('/');
      } catch (error) {
        console.error('Błąd logowania:', error);
        throw error;
      }
    },

    logout() {
      // Usuwamy token z Pinii i przeglądarki
      this.token = null;
      localStorage.removeItem('token');
      // Wyrzucamy na ekran logowania
      router.push('/login');
    }
  }
});