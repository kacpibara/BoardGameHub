<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import api from '../api/axios';
import Navbar from '../components/Navbar.vue';

const activeTab = ref('rentals'); 

const allRentals = ref<any[]>([]);
const allReservations = ref<any[]>([]);
const searchQuery = ref('');

const fetchAllRentals = async () => {
  try {
    const response = await api.get('/rentals/all');
    allRentals.value = response.data;
  } catch (error) { console.error("Brak uprawnień do wypożyczeń"); }
};

const processReturn = async (rentalId: number) => {
  if (!confirm("Czy zatwierdzasz zwrot gry?")) return;
  try {
    const response = await api.patch(`/rentals/${rentalId}/return`);
    alert(response.data); 
    fetchAllRentals(); 
  } catch (error: any) { alert("Błąd zwrotu."); }
};

const activeRentals = computed(() => {
  return allRentals.value.filter(r => 
    (r.status === 'ACTIVE' || r.status === 'LATE') &&
    r.gameTitle?.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

const fetchAllReservations = async () => {
  try {
    const response = await api.get('/table-reservations/all');
    allReservations.value = response.data;
  } catch (error) { console.error("Brak uprawnień do rezerwacji"); }
};

const updateReservation = async (id: number, status: string) => {
  const actionName = status === 'COMPLETED' ? 'zakończyć' : 'anulować (klient nieobecny)';
  if (!confirm(`Czy na pewno chcesz ${actionName} tę rezerwację? Gra wróci do puli.`)) return;

  try {
    const response = await api.patch(`/table-reservations/${id}/status?status=${status}`);
    alert(response.data);
    fetchAllReservations();
  } catch (error: any) { alert("Błąd zmiany statusu."); }
};

const activeReservations = computed(() => {
  return allReservations.value.filter(res => res.status === 'ACTIVE');
});

onMounted(() => {
  fetchAllRentals();
  fetchAllReservations();
});
</script>

<template>
  <div class="min-h-screen bg-gray-50 pb-20">
    <Navbar />

    <main class="max-w-7xl mx-auto p-8 mt-10">
      <header class="mb-10 flex flex-col md:flex-row justify-between items-end gap-4">
        <div>
          <h2 class="text-3xl font-extrabold text-indigo-900">Centrum Dowodzenia 👨‍💼</h2>
          <p class="text-gray-500 mt-2">Zarządzaj zwrotami i stolikami na sali.</p>
        </div>
        
        <div class="flex bg-gray-200 p-1 rounded-xl">
          <button 
            @click="activeTab = 'rentals'" 
            :class="activeTab === 'rentals' ? 'bg-white shadow-sm text-indigo-700' : 'text-gray-500 hover:text-gray-700'"
            class="px-6 py-2.5 rounded-lg font-bold text-sm transition-all">
            📦 Oczekujące Zwroty
          </button>
          <button 
            @click="activeTab = 'reservations'" 
            :class="activeTab === 'reservations' ? 'bg-white shadow-sm text-indigo-700' : 'text-gray-500 hover:text-gray-700'"
            class="px-6 py-2.5 rounded-lg font-bold text-sm transition-all">
            🪑 Stoliki na Sali
          </button>
        </div>
      </header>

      <div v-if="activeTab === 'rentals'" class="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
        <div class="p-4 bg-indigo-50 border-b border-indigo-100">
          <input v-model="searchQuery" type="text" placeholder="Szukaj po tytule gry..." class="px-4 py-2 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-indigo-500 w-full max-w-sm">
        </div>
        <table class="w-full text-left">
          <thead class="bg-gray-50 border-b border-gray-100">
            <tr>
              <th class="px-6 py-4 text-sm font-semibold text-gray-600">Gra</th>
              <th class="px-6 py-4 text-sm font-semibold text-gray-600">Termin zwrotu</th>
              <th class="px-6 py-4 text-sm font-semibold text-gray-600">Status</th>
              <th class="px-6 py-4 text-sm font-semibold text-gray-600 text-right">Akcja</th> 
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-100">
            <tr v-for="rental in activeRentals" :key="rental.id" class="hover:bg-gray-50 transition">
              <td class="px-6 py-4 font-bold text-gray-800">{{ rental.gameTitle }}</td>
              <td class="px-6 py-4 font-mono text-sm" :class="rental.status === 'LATE' ? 'text-red-600 font-bold' : 'text-gray-600'">{{ rental.dueDate }}</td>
              <td class="px-6 py-4">
                <span :class="rental.status === 'ACTIVE' ? 'bg-emerald-100 text-emerald-700' : 'bg-red-100 text-red-700'" class="px-3 py-1 rounded-full text-xs font-bold uppercase">
                  {{ rental.status }}
                </span>
              </td>
              <td class="px-6 py-4 text-right">
                <button @click="processReturn(rental.id)" class="px-4 py-2 bg-indigo-600 text-white text-xs font-bold rounded-lg hover:bg-indigo-700 transition shadow-sm">
                  Odbierz Grę
                </button>
              </td>
            </tr>
            <tr v-if="activeRentals.length === 0">
              <td colspan="4" class="px-6 py-10 text-center text-gray-500 italic">Brak gier oczekujących na zwrot.</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div v-if="activeTab === 'reservations'" class="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
        <table class="w-full text-left">
          <thead class="bg-gray-50 border-b border-gray-100">
            <tr>
              <th class="px-6 py-4 text-sm font-semibold text-gray-600">Data i Czas</th>
              <th class="px-6 py-4 text-sm font-semibold text-gray-600">Stolik</th>
              <th class="px-6 py-4 text-sm font-semibold text-gray-600">Przygotuj Grę</th>
              <th class="px-6 py-4 text-sm font-semibold text-gray-600 text-right">Zarządzaj</th> 
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-100">
            <tr v-for="res in activeReservations" :key="res.id" class="hover:bg-gray-50 transition">
              <td class="px-6 py-4">
                <div class="font-bold text-gray-800">{{ res.reservationDate }}</div>
                <div class="text-sm text-indigo-600 font-bold">{{ res.startTime }} ({{ res.durationHours }}h)</div>
              </td>
              <td class="px-6 py-4 font-bold text-gray-700">Nr {{ res.cafeTable?.tableNumber }}</td>
              <td class="px-6 py-4">
                <span v-if="res.boardGame" class="bg-yellow-100 text-yellow-800 px-3 py-1 rounded-lg text-sm font-bold border border-yellow-200">
                  🎲 {{ res.boardGame.title }}
                </span>
                <span v-else class="text-gray-400 italic text-sm">Klient wybierze na miejscu</span>
              </td>
              <td class="px-6 py-4 text-right space-x-2">
                <button @click="updateReservation(res.id, 'COMPLETED')" class="px-3 py-2 bg-emerald-100 text-emerald-700 text-xs font-bold rounded-lg hover:bg-emerald-200 transition">
                  Zakończ
                </button>
                <button @click="updateReservation(res.id, 'NO_SHOW')" class="px-3 py-2 bg-red-100 text-red-700 text-xs font-bold rounded-lg hover:bg-red-200 transition">
                  Nieobecny
                </button>
              </td>
            </tr>
            <tr v-if="activeReservations.length === 0">
              <td colspan="4" class="px-6 py-10 text-center text-gray-500 italic">Brak aktywnych rezerwacji na stoliki.</td>
            </tr>
          </tbody>
        </table>
      </div>
    </main>
  </div>
</template>