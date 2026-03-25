<script setup lang="ts">
import { ref, onMounted } from 'vue';
import api from '../api/axios';
import { useAuthStore } from '../stores/authStore';
import { useRouter } from 'vue-router';
import Navbar from '../components/Navbar.vue';

const authStore = useAuthStore();
const router = useRouter();

const userPoints = ref(0);
const games = ref<any[]>([]);

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
    userPoints.value = userRes.data.LoyaltyPoints || 0;

    const gamesRes = await api.get('/games');
    
    games.value = gamesRes.data
      .map((item: any) => item.game)
      .filter((g: any) => g.totalCopies > 0);

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
    <Navbar></Navbar>

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