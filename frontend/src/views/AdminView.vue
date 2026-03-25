<script setup lang="ts">
import { ref, onMounted } from 'vue';
import api from '../api/axios';
import Navbar from '../components/Navbar.vue';
import Chart from 'chart.js/auto';

const stats = ref({
  totalGames: 0,
  activeRentals: 0,
  totalPenalties: 0,
  rentalsByCategory: {} as Record<string, number>
});

const categoryChartCanvas = ref<HTMLCanvasElement | null>(null);

const fetchStats = async () => {
  try {
    const response = await api.get('/admin/stats');
    stats.value = response.data;
    renderCharts();
  } catch (error) {
    console.error("Błąd pobierania statystyk", error);
  }
};

const renderCharts = () => {
  if (!categoryChartCanvas.value) return;

  const ctx = categoryChartCanvas.value.getContext('2d');
  if (!ctx) return;

  const labels = Object.keys(stats.value.rentalsByCategory);
  const data = Object.values(stats.value.rentalsByCategory);

  new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: labels.length > 0 ? labels : ['Brak danych'],
      datasets: [{
        label: 'Wypożyczenia wg Kategorii',
        data: data.length > 0 ? data : [1],
        backgroundColor: [
          '#10B981', '#3B82F6', '#F59E0B', '#EF4444', '#8B5CF6'
        ],
        borderWidth: 0,
      }]
    },
    options: {
      responsive: true,
      plugins: {
        legend: { position: 'bottom' }
      }
    }
  });
};

onMounted(() => {
  fetchStats();
});
</script>

<template>
  <div class="min-h-screen bg-gray-50 pb-20">
    <Navbar />

    <main class="max-w-6xl mx-auto p-8 mt-10">
      <header class="mb-10">
        <h2 class="text-3xl font-extrabold text-gray-900">Dashboard Admina 📈</h2>
        <p class="text-gray-500 mt-2">Przegląd statystyk i przychodów kawiarni.</p>
      </header>

      <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-10">
        <div class="bg-white p-6 rounded-3xl shadow-sm border border-gray-100 flex items-center gap-4">
          <div class="p-4 bg-blue-50 text-blue-600 rounded-2xl text-2xl">🎲</div>
          <div>
            <p class="text-gray-500 text-sm font-bold uppercase tracking-wide">Gry w bazie</p>
            <p class="text-3xl font-black text-gray-900">{{ stats.totalGames }}</p>
          </div>
        </div>
        
        <div class="bg-white p-6 rounded-3xl shadow-sm border border-gray-100 flex items-center gap-4">
          <div class="p-4 bg-emerald-50 text-emerald-600 rounded-2xl text-2xl">📦</div>
          <div>
            <p class="text-gray-500 text-sm font-bold uppercase tracking-wide">Aktywne Wypożyczenia</p>
            <p class="text-3xl font-black text-gray-900">{{ stats.activeRentals }}</p>
          </div>
        </div>

        <div class="bg-white p-6 rounded-3xl shadow-sm border border-gray-100 flex items-center gap-4">
          <div class="p-4 bg-red-50 text-red-600 rounded-2xl text-2xl">💰</div>
          <div>
            <p class="text-gray-500 text-sm font-bold uppercase tracking-wide">Przychód z Kar</p>
            <p class="text-3xl font-black text-gray-900">{{ stats.totalPenalties.toFixed(2) }} zł</p>
          </div>
        </div>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
        <div class="bg-white p-8 rounded-3xl shadow-sm border border-gray-100">
          <h3 class="text-xl font-bold text-gray-800 mb-6">Popularność Kategorii</h3>
          <div class="relative h-64 flex justify-center">
            <canvas ref="categoryChartCanvas"></canvas> 
          </div>
        </div>
        
        <div class="bg-white p-8 rounded-3xl shadow-sm border border-gray-100 flex flex-col justify-center items-center text-center">
          <div class="text-6xl mb-4">🚀</div>
          <h3 class="text-xl font-bold text-gray-800 mb-2">Panel Sterowania</h3>
          <p class="text-gray-500 mb-6">Tutaj możesz w przyszłości dodać przyciski do dodawania nowych gier, edycji użytkowników czy generowania raportów PDF.</p>
          </div>
      </div>

    </main>
  </div>
</template>