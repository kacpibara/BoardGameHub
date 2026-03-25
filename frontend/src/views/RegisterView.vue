<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import api from '../api/axios';

const router = useRouter();
const firstName = ref('');
const lastName = ref('');
const email = ref('');
const password = ref('');
const errorMsg = ref('');
const successMsg = ref('');

const handleRegister = async () => {
  errorMsg.value = '';
  successMsg.value = '';
  
  try {
    await api.post('/auth/register', {
      firstName: firstName.value,
      lastName: lastName.value,
      email: email.value,
      password: password.value
    });
    
    successMsg.value = 'Konto utworzone pomyślnie! Zaraz zostaniesz przekierowany...';
    
    setTimeout(() => {
      router.push('/login');
    }, 2000);
    
  } catch (err: any) {
    errorMsg.value = 'Błąd rejestracji. Sprawdź dane lub spróbuj inny email.';
    console.error(err);
  }
};
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100 px-4">
    <div class="max-w-md w-full bg-white rounded-xl shadow-lg p-8">
      <div class="text-center mb-8">
        <h2 class="text-3xl font-bold text-gray-800">Stwórz konto 🎲</h2>
        <p class="text-gray-500 mt-2">Dołącz do społeczności BoardGameHub</p>
      </div>

      <form @submit.prevent="handleRegister" class="space-y-4">
        <div class="grid grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700">Imię</label>
            <input v-model="firstName" type="text" required 
              class="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-lg focus:ring-emerald-500 focus:border-emerald-500 outline-none">
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700">Nazwisko</label>
            <input v-model="lastName" type="text" required 
              class="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-lg focus:ring-emerald-500 focus:border-emerald-500 outline-none">
          </div>
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700">Email</label>
          <input v-model="email" type="email" required 
            class="mt-1 block w-full px-4 py-2 bg-gray-50 border border-gray-300 rounded-lg focus:ring-emerald-500 focus:border-emerald-500 outline-none">
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700">Hasło</label>
          <input v-model="password" type="password" required 
            class="mt-1 block w-full px-4 py-2 bg-gray-50 border border-gray-300 rounded-lg focus:ring-emerald-500 focus:border-emerald-500 outline-none">
        </div>

        <div v-if="errorMsg" class="text-red-500 text-sm bg-red-50 p-2 rounded border border-red-200">
          {{ errorMsg }}
        </div>
        <div v-if="successMsg" class="text-emerald-600 text-sm bg-emerald-50 p-2 rounded border border-emerald-200">
          {{ successMsg }}
        </div>

        <button type="submit" 
          class="w-full bg-emerald-600 hover:bg-emerald-700 text-white font-bold py-3 rounded-lg shadow-md transition">
          Zarejestruj się
        </button>
      </form>

      <div class="mt-6 text-center">
        <p class="text-sm text-gray-600">
          Masz już konto? 
          <router-link to="/login" class="text-emerald-600 font-semibold hover:underline">Zaloguj się</router-link>
        </p>
      </div>
    </div>
  </div>
</template>