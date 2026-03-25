<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue'; 
import api from '../api/axios';
import Navbar from '../components/Navbar.vue';

const games = ref<any[]>([]);
const searchQuery = ref(''); 
const selectedCategory = ref(''); 

const currentPage = ref(1);
const itemsPerPage = 6;

const rentModal = ref({ isOpen: false, game: null as any, usePoints: false });
const notification = ref({ isOpen: false, message: '', isSuccess: true });
const detailsModal = ref({ isOpen: false, gameItem: null as any, reviews: [] as any[], isLoading: false });
const userPoints = ref(0);

const fetchUserData = async () => {
  try {
    const response = await api.get('/clients/me');
    userPoints.value = response.data.LoyaltyPoints || 0;
  } catch (error) {}
};

const fetchGames = async () => {
  try {
    const response = await api.get('/games');
    games.value = response.data;
  } catch (error) { console.error('Błąd pobierania gier:', error); }
};

const openGameDetails = async (item: any) => {
  detailsModal.value = { isOpen: true, gameItem: item, reviews: [], isLoading: true };
  try {
    const res = await api.get(`/reviews/game/${item.game.id}`);
    detailsModal.value.reviews = res.data;
  } catch (error) {} finally {
    detailsModal.value.isLoading = false;
  }
};

const openRentModal = (game: any) => {
  rentModal.value = { isOpen: true, game: game, usePoints: userPoints.value >= 50 };
};

const confirmRent = async () => {
  try {
    await api.post('/rentals', { gameId: rentModal.value.game.id, useLoyaltyPoints: rentModal.value.usePoints });
    rentModal.value.isOpen = false;
    notification.value = { isOpen: true, message: "Sukces!", isSuccess: true };
    fetchGames();
    fetchUserData(); 
  } catch (error: any) {
    rentModal.value.isOpen = false;
    notification.value = { isOpen: true, message: "Błąd", isSuccess: false };
  }
};

const categories = computed(() => [...new Set(games.value.map(g => g.game.category).filter(c => c))]);

watch([searchQuery, selectedCategory], () => {
  currentPage.value = 1; 
});

const filteredGames = computed(() => {
  return games.value.filter(item => {
    const title = item.game.title || '';
    const category = item.game.category || '';
    return title.toLowerCase().includes(searchQuery.value.toLowerCase()) &&
           (selectedCategory.value === '' || category === selectedCategory.value);
  });
});

const totalPages = computed(() => {
  return Math.ceil(filteredGames.value.length / itemsPerPage) || 1;
});

const paginatedGames = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return filteredGames.value.slice(start, end);
});

onMounted(() => {
  fetchGames();
  fetchUserData();
});
</script>

<template>
  <div class="min-h-screen bg-gray-50 pb-20 relative">
    
    <div v-if="rentModal.isOpen" class="fixed inset-0 bg-gray-900/60 backdrop-blur-sm z-50 flex items-center justify-center p-4">
      <div class="bg-white rounded-3xl p-8 max-w-md w-full shadow-2xl">
        <div class="text-center mb-6">
          <div v-if="rentModal.usePoints" class="text-6xl mb-4">🎁</div>
          <div v-else class="text-6xl mb-4">🎲</div>
          <h3 class="text-2xl font-black text-gray-900">{{ rentModal.game?.title }}</h3>
          <p v-if="rentModal.usePoints" class="text-emerald-600 font-bold mt-2">Masz {{ userPoints }} pkt! Wypożycz za darmo (koszt: 50 pkt).</p>
          <p v-else class="text-gray-500 mt-2">Kaucja zwrotna wynosi: <span class="font-bold text-gray-800">{{ rentModal.game?.rentalPrice * 2 }} zł</span>.</p>
        </div>
        <div class="flex gap-4">
          <button @click="rentModal.isOpen = false" class="flex-1 py-3 bg-gray-100 font-bold rounded-xl hover:bg-gray-200">Anuluj</button>
          <button @click="confirmRent" class="flex-1 py-3 bg-emerald-600 text-white font-bold rounded-xl hover:bg-emerald-700 shadow-lg">Potwierdź</button>
        </div>
      </div>
    </div>

    <div v-if="notification.isOpen" class="fixed inset-0 bg-gray-900/60 backdrop-blur-sm z-[60] flex items-center justify-center p-4">
      <div class="bg-white rounded-3xl p-8 max-w-sm w-full shadow-2xl text-center">
        <div :class="notification.isSuccess ? 'text-emerald-500' : 'text-red-500'" class="text-6xl mb-4">{{ notification.isSuccess ? '✅' : '❌' }}</div>
        <p class="text-lg font-bold text-gray-800 mb-6">{{ notification.message }}</p>
        <button @click="notification.isOpen = false" class="w-full py-3 bg-gray-900 text-white font-bold rounded-xl hover:bg-gray-800">Zamknij</button>
      </div>
    </div>

    <div v-if="detailsModal.isOpen" class="fixed inset-0 bg-gray-900/80 backdrop-blur-md z-40 flex items-center justify-center p-4 md:p-10">
      <div class="bg-white rounded-3xl w-full max-w-5xl h-[85vh] shadow-2xl overflow-hidden flex flex-col md:flex-row relative">
        <button @click="detailsModal.isOpen = false" class="absolute top-4 right-4 z-10 bg-white/50 hover:bg-white text-gray-800 p-2 rounded-full backdrop-blur-sm transition">❌</button>

        <div class="md:w-1/2 bg-gray-50 flex flex-col">
          <div class="h-64 md:h-2/3 w-full relative">
            <img :src="detailsModal.gameItem.game.imageUrl || 'https://images.unsplash.com/photo-1610890716171-6b1bb98ffd09?q=80&w=600&auto=format&fit=crop'" class="w-full h-full object-cover" />
            <div class="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-black/80 to-transparent p-6 pt-20">
              <span class="bg-emerald-500 text-white px-3 py-1 rounded-full text-xs font-bold uppercase tracking-wider mb-2 inline-block">{{ detailsModal.gameItem.game.category }}</span>
              <h2 class="text-4xl font-black text-white">{{ detailsModal.gameItem.game.title }}</h2>
            </div>
          </div>
          <div class="p-8 flex-1">
            <div class="grid grid-cols-2 gap-4 text-gray-700">
              <div class="bg-white p-4 rounded-2xl shadow-sm border border-gray-100">
                <span class="text-2xl block mb-1">👥</span>
                <span class="font-bold">{{ detailsModal.gameItem.game.minPlayers }} - {{ detailsModal.gameItem.game.maxPlayers }}</span> graczy
              </div>
              <div class="bg-white p-4 rounded-2xl shadow-sm border border-gray-100">
                <span class="text-2xl block mb-1">💰</span>
                <span class="font-bold">{{ detailsModal.gameItem.game.rentalPrice }} zł</span> / dzień
              </div>
            </div>
            <button @click="openRentModal(detailsModal.gameItem.game); detailsModal.isOpen = false" :disabled="detailsModal.gameItem.game.totalCopies <= 0" class="w-full mt-8 font-black py-4 rounded-2xl transition-all shadow-lg disabled:opacity-50" :class="detailsModal.gameItem.game.totalCopies > 0 ? 'bg-gray-900 text-white hover:bg-emerald-600' : 'bg-gray-200 text-gray-400'">
              {{ detailsModal.gameItem.game.totalCopies > 0 ? 'Wypożycz tę grę' : 'Brak na stanie' }}
            </button>
          </div>
        </div>

        <div class="md:w-1/2 p-8 overflow-y-auto bg-white">
          <div class="flex items-center gap-3 mb-8 border-b border-gray-100 pb-4">
            <h3 class="text-2xl font-black text-gray-900">Opinie graczy</h3>
            <div class="flex items-center gap-1 bg-yellow-50 text-yellow-700 px-3 py-1 rounded-lg font-bold">
              ⭐ {{ detailsModal.gameItem.averageStars.toFixed(1) }} ({{ detailsModal.gameItem.reviewsCount }})
            </div>
          </div>

          <div v-if="detailsModal.isLoading" class="text-center text-gray-400 py-10 font-bold animate-pulse">Pobieranie opinii...</div>
          <div v-else-if="detailsModal.reviews.length === 0" class="text-center py-20">
            <div class="text-6xl mb-4">📭</div>
            <p class="text-gray-500 font-medium">Brak opinii. Bądź pierwszą osobą, która oceni tę grę!</p>
          </div>
          <div v-else class="space-y-6">
            <div v-for="(review, index) in detailsModal.reviews" :key="index" class="bg-gray-50 p-6 rounded-2xl border border-gray-100">
              <div class="flex justify-between items-start mb-3">
                <div class="font-bold text-gray-900">{{ review.authorName }}</div>
                <div class="text-xs text-gray-400 font-mono">{{ review.reviewDate }}</div>
              </div>
              <div class="text-yellow-500 text-sm mb-3">
                {{ '⭐'.repeat(review.stars) }}{{ '☆'.repeat(5 - review.stars) }}
              </div>
              <p class="text-gray-600 leading-relaxed italic">"{{ review.comment }}"</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <Navbar />

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

      <div v-if="paginatedGames.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
        <div v-for="item in paginatedGames" :key="item.game.id" class="bg-white rounded-3xl shadow-sm border border-gray-100 overflow-hidden hover:shadow-2xl transition-all group">
          
          <div @click="openGameDetails(item)" class="relative h-56 overflow-hidden bg-gray-200 cursor-pointer">
            <img :src="item.game.imageUrl || 'https://images.unsplash.com/photo-1610890716171-6b1bb98ffd09?q=80&w=600&auto=format&fit=crop'" class="w-full h-full object-cover group-hover:scale-110 transition duration-500" />
            <div class="absolute top-4 right-4 bg-white/90 backdrop-blur-sm px-3 py-1 rounded-full text-[10px] font-black uppercase text-emerald-600 shadow-sm">{{ item.game.category }}</div>
            <div class="absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center backdrop-blur-[2px]">
               <span class="text-white font-bold tracking-widest uppercase">Pokaż szczegóły</span>
            </div>
          </div>
          
          <div class="p-8">
            <div class="flex items-center gap-1.5 mb-3 text-sm">
              <span class="text-yellow-500 text-base">⭐</span>
              <span class="font-bold text-gray-900">{{ item.averageStars.toFixed(1) }}</span>
              <span class="text-gray-500">({{ item.reviewsCount }} opinii)</span>
            </div>
            <h3 @click="openGameDetails(item)" class="text-2xl font-bold text-gray-800 mb-4 cursor-pointer hover:text-emerald-600 transition">{{ item.game.title }}</h3>
            <div class="space-y-3 text-gray-500 mb-8">
              <p>👥 {{ item.game.minPlayers }}-{{ item.game.maxPlayers }} graczy</p>
              <p>💰 {{ item.game.rentalPrice }} zł / dzień</p>
              <p :class="item.game.totalCopies > 0 ? 'text-emerald-600' : 'text-red-500'">📦 {{ item.game.totalCopies > 0 ? `Dostępne: ${item.game.totalCopies} szt.` : 'Brak na stanie' }}</p>
            </div>
            <button @click="openRentModal(item.game)" :disabled="item.game.totalCopies <= 0" class="w-full font-black py-4 rounded-2xl transition-all shadow-lg disabled:opacity-50" :class="item.game.totalCopies > 0 ? 'bg-gray-900 text-white hover:bg-emerald-600' : 'bg-gray-200 text-gray-400'">
              {{ item.game.totalCopies > 0 ? 'Wypożycz teraz' : 'Brak na stanie' }}
            </button>
          </div>
        </div>
      </div>

      <div v-if="totalPages > 1" class="mt-12 flex justify-center items-center gap-4">
        <button 
          @click="currentPage--" 
          :disabled="currentPage === 1"
          class="px-6 py-3 rounded-xl font-bold transition-all"
          :class="currentPage === 1 ? 'bg-gray-100 text-gray-400 cursor-not-allowed' : 'bg-white text-gray-800 shadow-sm border border-gray-200 hover:border-emerald-500 hover:text-emerald-600'">
          ⬅ Poprzednia
        </button>
        
        <span class="text-gray-500 font-medium">
          Strona <span class="font-bold text-gray-900">{{ currentPage }}</span> z {{ totalPages }}
        </span>
        
        <button 
          @click="currentPage++" 
          :disabled="currentPage === totalPages"
          class="px-6 py-3 rounded-xl font-bold transition-all"
          :class="currentPage === totalPages ? 'bg-gray-100 text-gray-400 cursor-not-allowed' : 'bg-white text-gray-800 shadow-sm border border-gray-200 hover:border-emerald-500 hover:text-emerald-600'">
          Następna ➡
        </button>
      </div>

    </main>
  </div>
</template>