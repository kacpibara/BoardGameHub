<script setup lang="ts">
import { ref, onMounted } from 'vue';
import api from '../api/axios';
import Navbar from '../components/Navbar.vue';

const reservations = ref<any[]>([]);

const fetchMyReservations = async () => {
  try {
    const response = await api.get('/table-reservations/my');
    reservations.value = response.data;
  } catch (error) {
    console.error('Błąd pobierania rezerwacji');
  }
};

const cancelReservation = async (id: number) => {
  const confirmed = confirm("Czy na pewno chcesz anulować tę rezerwację?");
  if (!confirmed) return;

  try {
    const response = await api.patch(`/table-reservations/${id}/cancel`);
    alert(response.data);
    fetchMyReservations();
  } catch (error: any) {
    alert(error.response?.data || "Błąd podczas anulowania rezerwacji.");
  }
};

onMounted(() => {
  fetchMyReservations();
});
</script>

<template>
  <div class="min-h-screen bg-gray-50 pb-20">
    
    <Navbar />

    <main class="max-w-6xl mx-auto p-8 mt-10">
      <header class="mb-10">
        <h2 class="text-3xl font-bold text-gray-800">Twoje Rezerwacje Stolików 🪑</h2>
        <p class="text-gray-600">Zarządzaj swoimi planowanymi wizytami w lokalu.</p>
      </header>

      <div class="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
        <table class="w-full text-left">
          <thead class="bg-gray-50 border-b border-gray-100">
            <tr>
              <th class="px-6 py-4 text-sm font-semibold text-gray-600">Kiedy</th>
              <th class="px-6 py-4 text-sm font-semibold text-gray-600">Stolik</th>
              <th class="px-6 py-4 text-sm font-semibold text-gray-600">Przygotowana Gra</th>
              <th class="px-6 py-4 text-sm font-semibold text-gray-600">Status</th>
              <th class="px-6 py-4 text-sm font-semibold text-gray-600 text-right">Akcja</th> 
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-100">
            <tr v-for="res in reservations" :key="res.id" class="hover:bg-gray-50 transition">
              <td class="px-6 py-4">
                <div class="font-bold text-gray-800">{{ res.reservationDate }}</div>
                <div class="text-sm text-gray-500">{{ res.startTime }} ({{ res.durationHours }} godz.)</div>
              </td>
              <td class="px-6 py-4 font-bold text-gray-700">Nr {{ res.cafeTable?.tableNumber }}</td>
              <td class="px-6 py-4 text-gray-600 italic">
                {{ res.boardGame ? res.boardGame.title : 'Brak (wybór na miejscu)' }}
              </td>
              <td class="px-6 py-4">
                <span :class="res.status === 'ACTIVE' ? 'bg-emerald-100 text-emerald-700' : 'bg-red-100 text-red-700'" class="px-3 py-1 rounded-full text-xs font-bold uppercase">
                  {{ res.status }}
                </span>
              </td>
              <td class="px-6 py-4 text-right">
                <button 
                  v-if="res.status === 'ACTIVE'"
                  @click="cancelReservation(res.id)"
                  class="px-4 py-2 bg-red-100 text-red-700 text-xs font-bold rounded-lg hover:bg-red-200 transition"
                >
                  Odwołaj
                </button>
              </td>
            </tr>
            <tr v-if="reservations.length === 0">
              <td colspan="5" class="px-6 py-10 text-center text-gray-500 italic">Nie masz zaplanowanych wizyt w lokalu.</td>
            </tr>
          </tbody>
        </table>
      </div>
    </main>
  </div>
</template>