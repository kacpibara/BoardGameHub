<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import api from '../api/axios';
import { useAuthStore } from '../stores/authStore';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const router = useRouter();

const games = ref<any[]>([]);
const searchQuery = ref(''); 
const selectedCategory = ref(''); 
const userRole = ref('');
const userPoints = ref(0);

const fetchGames = async () => {
  try {
    const response = await api.get('/games');
    games.value = response.data;
  } catch (error) {
    console.error('Błąd pobierania gier:', error);
  }
};

const fetchUserData = async () => {
  try {
    const response = await api.get('/clients/me');
    userPoints.value = response.data.LoyaltyPoints || 0;
    userRole.value = response.data.role; // Zapisujemy rolę (np. 'ADMIN' lub 'USER')
  } catch (error) {
    console.error("Błąd pobierania danych profilu");
  }
};

const rentGame = async (game: any) => {
  const confirmed = confirm(`Czy na pewno chcesz wypożyczyć grę "${game.title}"? Koszt kaucji to ${game.rentalPrice * 2} zł.`);
  if (!confirmed) return;

  try {
    await api.post('/rentals', { gameId: game.id });
    alert("Gra wypożyczona! Przyznano 10 punktów lojalnościowych. ⭐");
    fetchGames();
    fetchUserData(); 
  } catch (error: any) {
    alert(error.response?.data || "Błąd wypożyczania.");
  }
};

const filteredGames = computed(() => {
  return games.value.filter(game => {
    const title = game.title || '';
    const category = game.category || '';
    const matchesSearch = title.toLowerCase().includes(searchQuery.value.toLowerCase());
    const matchesCategory = selectedCategory.value === '' || category === selectedCategory.value;
    return matchesSearch && matchesCategory;
  });
});

const categories = computed(() => {
  const allCats = games.value.map(g => g.category).filter(c => c);
  return [...new Set(allCats)];
});

onMounted(() => {
  fetchGames();
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
          <router-link to="/" class="text-emerald-600 font-bold">Katalog Gier</router-link>
          <router-link to="/my-rentals" class="text-gray-600 hover:text-emerald-600 font-medium transition">Moje Wypożyczenia</router-link>
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
        <h2 class="text-4xl font-extrabold text-gray-900 tracking-tight">Dostępne gry 🏰</h2>
      </header>

      <div class="flex flex-col md:flex-row gap-4 mb-10 bg-white p-5 rounded-2xl shadow-sm border border-gray-100">
        <input v-model="searchQuery" type="text" placeholder="Szukaj gry..." class="flex-1 px-4 py-2.5 bg-gray-50 border border-gray-200 rounded-xl outline-none focus:ring-2 focus:ring-emerald-500">
        <select v-model="selectedCategory" class="px-4 py-2.5 bg-gray-50 border border-gray-200 rounded-xl outline-none focus:ring-2 focus:ring-emerald-500">
          <option value="">Wszystkie kategorie</option>
          <option v-for="cat in categories" :key="cat" :value="cat">{{ cat }}</option>
        </select>
      </div>

      <div v-if="filteredGames.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
        <div v-for="game in filteredGames" :key="game.id" class="bg-white rounded-3xl shadow-sm border border-gray-100 overflow-hidden hover:shadow-2xl transition-all group">
          <div class="relative h-56 overflow-hidden bg-gray-200">
            <img :src="game.imageUrl || 'https://images.unsplash.com/photo-1610890716171-6b1bb98ffd09?q=80&w=600&auto=format&fit=crop'" class="w-full h-full object-cover group-hover:scale-110 transition duration-500" />
            <div class="absolute top-4 right-4 bg-white/90 backdrop-blur-sm px-3 py-1 rounded-full text-[10px] font-black uppercase text-emerald-600 shadow-sm">
              {{ game.category }}
            </div>
          </div>
          
          <div class="p-8">
            <h3 class="text-2xl font-bold text-gray-800 mb-4">{{ game.title }}</h3>
            <div class="space-y-3 text-gray-500 mb-8">
              <p>👥 {{ game.minPlayers }}-{{ game.maxPlayers }} graczy</p>
              <p>💰 {{ game.rentalPrice }} zł / dzień</p>
              <p :class="game.totalCopies > 0 ? 'text-emerald-600' : 'text-red-500'">
                📦 {{ game.totalCopies > 0 ? `Dostępne: ${game.totalCopies} szt.` : 'Brak na stanie' }}
              </p>
            </div>
            <button @click="rentGame(game)" :disabled="game.totalCopies <= 0" class="w-full font-black py-4 rounded-2xl transition-all shadow-lg disabled:opacity-50" :class="game.totalCopies > 0 ? 'bg-gray-900 text-white hover:bg-emerald-600' : 'bg-gray-200 text-gray-400'">
              {{ game.totalCopies > 0 ? 'Wypożycz teraz' : 'Brak na stanie' }}
            </button>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>