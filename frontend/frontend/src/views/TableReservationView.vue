<script setup lang="ts">
import { ref, onMounted } from 'vue';
import api from '../api/axios';
import { useAuthStore } from '../stores/authStore';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const router = useRouter();

const userPoints = ref(0);
const games = ref<any[]>([]);

// Pola formularza
const form = ref({
  tableId: 1,
  reservationDate: '',
  startTime: '16:00',
  durationHours: 2,
  optionalGameId: ''
});

const fetchData = async () => {
  try {
    const userRes = await api.get('/clients/me');
    userPoints.value = userRes.data.LoyaltyPoints || 0; // Poprawka nazwy!

    const gamesRes = await api.get('/games');
    games.value = gamesRes.data.filter((g: any) => g.totalCopies > 0);
  } catch (error) {
    console.error("Błąd pobierania danych:", error);
  }
};

const submitReservation = async () => {
  if (!form.value.reservationDate) {
    alert("Wybierz datę rezerwacji!");
    return;
  }

  try {
    const payload = {
      tableId: form.value.tableId,
      reservationDate: form.value.reservationDate,
      startTime: form.value.startTime,
      durationHours: form.value.durationHours,
      optionalGameId: form.value.optionalGameId ? Number(form.value.optionalGameId) : null
    };

    // Zmieniona ścieżka, skoro mówiłeś, że klasy na backu to CafeTableReservation
    const response = await api.post('/table-reservations', payload); 
    alert("Sukces! " + response.data);
    router.push('/my-rentals');
  } catch (error: any) {
    alert(error.response?.data || "Błąd podczas rezerwacji. Sprawdź dostępność.");
  }
};

onMounted(fetchData);
</script>

<template>
<div class="min-h-screen bg-gray-50 pb-20">
    <nav class="bg-white shadow-sm px-6 py-4 flex justify-between items-center border-b border-gray-200">
      <div class="flex items-center gap-6">
        <h1 class="text-2xl font-black text-emerald-600 tracking-tight cursor-pointer" @click="$router.push('/')">BoardGameHub</h1>
        <div class="flex items-center gap-2 bg-emerald-50 px-4 py-2 rounded-full border border-emerald-100 shadow-sm min-w-[90px] justify-center">
          <span class="text-lg leading-none">⭐</span>
          <span class="text-emerald-700 font-extrabold text-sm whitespace-nowrap">{{ userPoints }} pkt</span>
        </div>
      </div>
              <div class="hidden md:flex gap-4">
        <router-link to="/" class="text-gray-600 hover:text-emerald-600 font-medium transition">Katalog Gier</router-link>
          <router-link to="/my-rentals" class="text-gray-600 hover:text-emerald-600 font-medium transition">Moje Wypożyczenia</router-link>
          <router-link to="/reserve" class="text-emerald-600 font-bold">Zarezerwuj Stolik</router-link>
        
 <router-link 
  v-if="userRole === 'ADMIN'" 
  to="/admin" 
  class="bg-gray-900 text-white px-4 py-1.5 rounded-lg font-bold hover:bg-gray-800 transition shadow-sm">
  Panel Admina ⚙️
</router-link>
</div>
      <div class="flex items-center gap-6">

        <button @click="authStore.logout()" class="text-gray-500 hover:text-red-600 font-medium transition">Wyloguj się</button>
      </div>
    </nav>

    <main class="max-w-3xl mx-auto p-8 mt-10">
      <header class="mb-10 text-center">
        <h2 class="text-4xl font-extrabold text-gray-900 tracking-tight">Zarezerwuj Stolik 🪑</h2>
        <p class="text-gray-500 mt-2 text-lg">Wybierz termin i poproś o przygotowanie gry na start!</p>
      </header>

      <div class="bg-white rounded-3xl shadow-xl border border-gray-100 overflow-hidden p-8 md:p-12">
        <form @submit.prevent="submitReservation" class="space-y-6">
          
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label class="block text-sm font-bold text-gray-700 mb-2">Data rezerwacji</label>
              <input type="date" v-model="form.reservationDate" required
                class="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl outline-none focus:ring-2 focus:ring-emerald-500 transition">
            </div>
            <div>
              <label class="block text-sm font-bold text-gray-700 mb-2">Godzina rozpoczęcia</label>
              <input type="time" v-model="form.startTime" required
                class="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl outline-none focus:ring-2 focus:ring-emerald-500 transition">
            </div>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label class="block text-sm font-bold text-gray-700 mb-2">Czas trwania (godziny)</label>
              <input type="number" min="1" max="8" v-model="form.durationHours" required
                class="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl outline-none focus:ring-2 focus:ring-emerald-500 transition">
            </div>
            <div>
              <label class="block text-sm font-bold text-gray-700 mb-2">Wybierz stolik</label>
              <select v-model="form.tableId" class="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl outline-none focus:ring-2 focus:ring-emerald-500 transition">
                <option :value="1">Stolik 1 (2-osobowy, przy oknie)</option>
                <option :value="2">Stolik 2 (4-osobowy, kanapa)</option>
                <option :value="3">Stolik 3 (4-osobowy, środek sali)</option>
                <option :value="4">Stolik 4 (6-osobowy, duży blat)</option>
                <option :value="5">Stolik 5 (8-osobowy, VIP room)</option>
              </select>
            </div>
          </div>

          <div class="pt-4 border-t border-gray-100">
            <label class="block text-sm font-bold text-gray-700 mb-2">Gra czekająca na stoliku (Opcjonalnie) 🎲</label>
            <select v-model="form.optionalGameId" class="w-full px-4 py-3 bg-gray-50 border border-emerald-200 rounded-xl outline-none focus:ring-2 focus:ring-emerald-500 transition text-emerald-800">
              <option value="">-- Przyjdę i wybiorę na miejscu --</option>
              <option v-for="game in games" :key="game.id" :value="game.id">
                {{ game.title }} (Dostępne sztuki: {{ game.totalCopies }})
              </option>
            </select>
            <p class="text-xs text-gray-400 mt-2">Gra zostanie automatycznie zdjęta ze stanu magazynowego na czas Twojej rezerwacji.</p>
          </div>

          <button type="submit" class="w-full mt-8 bg-gray-900 text-white font-black text-lg py-4 rounded-xl hover:bg-emerald-600 transition-all duration-300 shadow-lg hover:shadow-emerald-200 hover:-translate-y-1">
            Potwierdź rezerwację
          </button>

        </form>
      </div>
    </main>
  </div>
</template>