<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import api from '../api/axios';
import { useAuthStore } from '../stores/authStore';

const router = useRouter();
const authStore = useAuthStore();
const userPoints = ref(0);
const userRole = ref('');

const fetchUserData = async () => {
  try {
    const response = await api.get('/clients/me');
    userPoints.value = response.data.LoyaltyPoints || 0;
    userRole.value = response.data.role;
  } catch (error) {
    console.error("Błąd pobierania danych profilu", error);
  }
};

onMounted(fetchUserData);
</script>

<template>
  <nav class="bg-white shadow-sm px-6 py-4 flex justify-between items-center border-b border-gray-200">
    <div class="flex items-center gap-6">
      <h1 class="text-2xl font-black text-emerald-600 tracking-tight cursor-pointer" @click="router.push('/')">BoardGameHub</h1>
      <div class="flex items-center gap-2 bg-emerald-50 px-4 py-2 rounded-full border border-emerald-100 shadow-sm min-w-[90px] justify-center">
        <span class="text-lg leading-none">⭐</span>
        <span class="text-emerald-700 font-extrabold text-sm whitespace-nowrap">{{ userPoints }} pkt</span>
      </div>
    </div>
    
    <div class="flex items-center gap-6">
      <div class="hidden md:flex gap-4 items-center">
        <router-link to="/" class="text-gray-600 hover:text-emerald-600 font-medium transition" active-class="text-emerald-600 font-bold">Katalog Gier</router-link>
        <router-link to="/my-rentals" class="text-gray-600 hover:text-emerald-600 font-medium transition" active-class="text-emerald-600 font-bold">Moje Wypożyczenia</router-link>
        
        <router-link to="/my-reservations" class="text-gray-600 hover:text-emerald-600 font-medium transition" active-class="text-emerald-600 font-bold">Moje Rezerwacje</router-link>
        
        <router-link to="/profile" class="text-gray-600 hover:text-emerald-600 font-medium transition" active-class="text-emerald-600 font-bold">Mój Profil</router-link>
        
        <router-link to="/reserve" class="text-gray-600 hover:text-emerald-600 font-medium transition" active-class="text-emerald-600 font-bold">Zarezerwuj Stolik</router-link>
        
        <router-link v-if="userRole === 'ADMIN'" to="/admin" class="bg-gray-900 text-white px-4 py-1.5 rounded-lg font-bold hover:bg-gray-800 transition shadow-sm ml-2">Panel Admina ⚙️</router-link>
        <router-link v-if="userRole === 'ADMIN' || userRole === 'EMPLOYEE'" to="/employee" class="bg-indigo-100 text-indigo-800 px-4 py-1.5 rounded-lg font-bold hover:bg-indigo-200 transition shadow-sm ml-2">Panel Pracownika 👨‍💼</router-link>
      </div>
      <button @click="authStore.logout()" class="text-gray-500 hover:text-red-600 font-medium transition">Wyloguj</button>
    </div>
  </nav>
</template>