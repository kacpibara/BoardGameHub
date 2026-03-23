<script setup lang="ts">
import { ref, onMounted } from 'vue';
import api from '../api/axios';
import { useAuthStore } from '../stores/authStore';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const router = useRouter();

const rentals = ref<any[]>([]);
const userPoints = ref(0);
const userRole = ref('');

const fetchMyRentals = async () => {
  try {
    const response = await api.get('/rentals/my');
    rentals.value = response.data;
  } catch (error) {
    console.error('Błąd pobierania wypożyczeń:', error);
  }
};

const fetchUserData = async () => {
  try {
    const response = await api.get('/clients/me');
    userPoints.value = response.data.LoyaltyPoints || 0;
    userRole.value = response.data.role;
  } catch (error) {
    console.error("Błąd pobierania punktów");
  }
};

const returnGame = async (rentalId: number) => {
  try {
    const response = await api.patch(`/rentals/${rentalId}/return`);
    alert(response.data);
    fetchMyRentals(); 
  } catch (error) {
    alert("Błąd podczas zwrotu gry.");
  }
};

onMounted(() => {
  fetchMyRentals();
  fetchUserData();
});
</script>

<template>
  <div class="min-h-screen bg-gray-50 pb-20">
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
          <router-link to="/" class="text-gray-600 hover:text-emerald-600 font-medium transition">Katalog Gier</router-link>
          <router-link to="/my-rentals" class="text-emerald-600 font-bold">Moje Wypożyczenia</router-link>
          <router-link to="/reserve" class="text-gray-600 hover:text-emerald-600 font-medium transition">Zarezerwuj Stolik</router-link>
        
          <router-link 
            v-if="userRole === 'ADMIN'" 
            to="/admin" 
            class="bg-gray-900 text-white px-4 py-1.5 rounded-lg font-bold hover:bg-gray-800 transition shadow-sm ml-2">
            Panel Admina ⚙️
          </router-link>
        </div>
        <button @click="authStore.logout()" class="text-gray-500 hover:text-red-600 font-medium transition">Wyloguj się</button>
      </div>
    </nav>

    <main class="max-w-7xl mx-auto p-8 mt-10">
      <header class="mb-10">
        <h2 class="text-3xl font-bold text-gray-800">Twoja kolekcja 📚</h2>
        <p class="text-gray-600">Gry, które aktualnie masz u siebie.</p>
      </header>

      <div class="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
        <table class="w-full text-left">
          <thead class="bg-gray-50 border-b border-gray-100">
            <tr>
              <th class="px-6 py-4 text-sm font-semibold text-gray-600">Gra</th>
              <th class="px-6 py-4 text-sm font-semibold text-gray-600">Data wypożyczenia</th>
              <th class="px-6 py-4 text-sm font-semibold text-gray-600">Termin zwrotu</th>
              <th class="px-6 py-4 text-sm font-semibold text-gray-600">Status</th>
              <th class="px-6 py-4 text-sm font-semibold text-gray-600">Akcje</th> 
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-100">
            <tr v-for="rental in rentals" :key="rental.id" class="hover:bg-gray-50 transition">
              <td class="px-6 py-4 font-bold text-gray-800">{{ rental.gameTitle }}</td>
              <td class="px-6 py-4 text-gray-600">{{ rental.rentalDate }}</td>
              <td class="px-6 py-4 text-gray-600">{{ rental.dueDate }}</td>
              <td class="px-6 py-4">
                <span :class="{
                  'bg-emerald-100 text-emerald-700': rental.status === 'ACTIVE',
                  'bg-gray-100 text-gray-700': rental.status === 'RETURNED',
                  'bg-red-100 text-red-700': rental.status === 'LATE'
                }" class="px-3 py-1 rounded-full text-xs font-bold uppercase">
                  {{ rental.status }}
                </span>
              </td>
              <td class="px-6 py-4">
                <button 
                  v-if="rental.status === 'ACTIVE'"
                  @click="returnGame(rental.id)"
                  class="text-xs font-bold text-emerald-600 hover:text-emerald-700 underline decoration-2 underline-offset-4"
                >
                  Oddaj grę
                </button>
                <span v-else class="text-xs text-gray-400">Brak akcji</span>
              </td>
            </tr>
            <tr v-if="rentals.length === 0">
              <td colspan="5" class="px-6 py-10 text-center text-gray-500 italic"> Nie masz jeszcze żadnych wypożyczonych gier.</td>
            </tr>
          </tbody>
        </table>
      </div>
    </main>
  </div>
</template>