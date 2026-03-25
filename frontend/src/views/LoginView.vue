<script setup lang="ts">
import { ref } from 'vue';
import { useAuthStore } from '../stores/authStore';

const authStore = useAuthStore();
const email = ref('');
const password = ref('');
const errorMsg = ref('');

const handleLogin = async () => {
  errorMsg.value = '';
  try {
    await authStore.login(email.value, password.value);
  } catch (err) {
    errorMsg.value = 'Błędny email lub hasło. Spróbuj ponownie.';
  }
};
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100 px-4">
    <div class="max-w-md w-full bg-white rounded-xl shadow-lg p-8">
      <div class="text-center mb-10">
        <h2 class="text-3xl font-bold text-gray-800">Witaj ponownie! 🎲</h2>
        <p class="text-gray-500 mt-2">Zaloguj się do BoardGameHub</p>
      </div>

      <form @submit.prevent="handleLogin" class="space-y-6">
        <div>
          <label class="block text-sm font-medium text-gray-700">Email</label>
          <input v-model="email" type="email" required 
            class="mt-1 block w-full px-4 py-3 bg-gray-50 border border-gray-300 rounded-lg focus:ring-emerald-500 focus:border-emerald-500 outline-none transition"
            placeholder="twoj@email.pl">
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700">Hasło</label>
          <input v-model="password" type="password" required 
            class="mt-1 block w-full px-4 py-3 bg-gray-50 border border-gray-300 rounded-lg focus:ring-emerald-500 focus:border-emerald-500 outline-none transition"
            placeholder="••••••••">
        </div>

        <div v-if="errorMsg" class="text-red-500 text-sm bg-red-50 p-3 rounded-lg border border-red-200">
          {{ errorMsg }}
        </div>

        <button type="submit" 
          class="w-full bg-emerald-600 hover:bg-emerald-700 text-white font-bold py-3 rounded-lg shadow-md transition transform hover:-translate-y-0.5">
          Zaloguj się
        </button>
      </form>

      <div class="mt-6 text-center">
        <p class="text-sm text-gray-600">
          Nie masz konta? 
          <router-link to="/register" class="text-emerald-600 font-semibold hover:underline">Zarejestruj się</router-link>        </p>
      </div>
    </div>
  </div>
</template>