<script setup lang="ts">
import { ref, onMounted } from 'vue';
import api from '../api/axios';
import { useAuthStore } from '../stores/authStore';
import { useRouter } from 'vue-router';
import Navbar from '../components/Navbar.vue';

const authStore = useAuthStore();
const router = useRouter();

const rentals = ref<any[]>([]);
const userPoints = ref(0);
const userRole = ref('');

const notification = ref({ isOpen: false, message: '', isSuccess: true });
const returnModal = ref({ isOpen: false, rentalId: null as any });

const reviewModal = ref({ isOpen: false, gameId: null as any, gameTitle: '' });
const reviewForm = ref({ stars: 5, comment: '' });

const fetchMyRentals = async () => {
  try {
    const response = await api.get('/rentals/my');
    rentals.value = response.data;
  } catch (error) { console.error('Błąd pobierania wypożyczeń'); }
};

const openReturnModal = (rentalId: number) => {
  returnModal.value = { isOpen: true, rentalId };
};

const confirmReturn = async () => {
  try {
    const response = await api.patch(`/rentals/${returnModal.value.rentalId}/return`);
    returnModal.value.isOpen = false;
    notification.value = { isOpen: true, message: response.data, isSuccess: true };
    fetchMyRentals(); 
  } catch (error: any) {
    returnModal.value.isOpen = false;
    notification.value = { isOpen: true, message: "Błąd podczas zwrotu gry.", isSuccess: false };
  }
};

const openReviewModal = (rental: any) => {
  reviewForm.value = { stars: 5, comment: '' };
  reviewModal.value = { isOpen: true, gameId: rental.gameId || 1, gameTitle: rental.gameTitle };
};

const submitReview = async () => {
  try {
    await api.post('/reviews', {
      gameId: reviewModal.value.gameId,
      stars: reviewForm.value.stars,
      comment: reviewForm.value.comment
    });
    reviewModal.value.isOpen = false;
    notification.value = { isOpen: true, message: "Dziękujemy za ocenę!", isSuccess: true };
  } catch (error) {
    reviewModal.value.isOpen = false;
    notification.value = { isOpen: true, message: "Błąd wysyłania opinii.", isSuccess: false };
  }
};

onMounted(() => {
  fetchMyRentals();
});
</script>

<template>
  <div class="min-h-screen bg-gray-50 pb-20 relative">

    <div v-if="notification.isOpen" class="fixed inset-0 bg-gray-900/60 backdrop-blur-sm z-50 flex items-center justify-center p-4">
      <div class="bg-white rounded-3xl p-8 max-w-sm w-full shadow-2xl text-center">
        <div :class="notification.isSuccess ? 'text-emerald-500' : 'text-red-500'" class="text-6xl mb-4">
          {{ notification.isSuccess ? '✅' : '❌' }}
        </div>
        <p class="text-lg font-bold text-gray-800 mb-6">{{ notification.message }}</p>
        <button @click="notification.isOpen = false" class="w-full py-3 bg-gray-900 text-white font-bold rounded-xl hover:bg-gray-800 transition">Zamknij</button>
      </div>
    </div>

    <div v-if="returnModal.isOpen" class="fixed inset-0 bg-gray-900/60 backdrop-blur-sm z-50 flex items-center justify-center p-4">
      <div class="bg-white rounded-3xl p-8 max-w-md w-full shadow-2xl text-center">
        <div class="text-6xl mb-4">📦</div>
        <h3 class="text-2xl font-black text-gray-900 mb-2">Oddajesz grę?</h3>
        <p class="text-gray-500 mb-8">Czy potwierdzasz, że gra jest kompletna i gotowa do zwrotu?</p>
        <div class="flex gap-4">
          <button @click="returnModal.isOpen = false" class="flex-1 py-3 bg-gray-100 text-gray-700 font-bold rounded-xl hover:bg-gray-200 transition">Anuluj</button>
          <button @click="confirmReturn" class="flex-1 py-3 bg-emerald-600 text-white font-bold rounded-xl hover:bg-emerald-700 transition shadow-lg">Tak, oddaję</button>
        </div>
      </div>
    </div>

    <div v-if="reviewModal.isOpen" class="fixed inset-0 bg-gray-900/60 backdrop-blur-sm z-50 flex items-center justify-center p-4">
      <div class="bg-white rounded-3xl p-8 max-w-md w-full shadow-2xl">
        <h3 class="text-2xl font-black text-gray-900 mb-2">Oceń: {{ reviewModal.gameTitle }}</h3>
        <p class="text-gray-500 mb-6 text-sm">Jak Ci się podobała gra?</p>
        
        <form @submit.prevent="submitReview" class="space-y-4">
          <div>
            <label class="block text-sm font-bold text-gray-700 mb-2">Twoja ocena (Gwiazdki)</label>
            <select v-model="reviewForm.stars" class="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl outline-none focus:ring-2 focus:ring-emerald-500">
              <option value="5">⭐⭐⭐⭐⭐ Uwielbiam!</option>
              <option value="4">⭐⭐⭐⭐ Bardzo dobra</option>
              <option value="3">⭐⭐⭐ OK, ale bez szału</option>
              <option value="2">⭐⭐ Taka sobie</option>
              <option value="1">⭐ Słaba</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-bold text-gray-700 mb-2">Krótki komentarz</label>
            <textarea v-model="reviewForm.comment" rows="3" required class="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl outline-none focus:ring-2 focus:ring-emerald-500" placeholder="Napisz kilka słów..."></textarea>
          </div>
          <div class="flex gap-4 mt-6">
            <button type="button" @click="reviewModal.isOpen = false" class="flex-1 py-3 bg-gray-100 text-gray-700 font-bold rounded-xl hover:bg-gray-200 transition">Anuluj</button>
            <button type="submit" class="flex-1 py-3 bg-yellow-500 text-white font-bold rounded-xl hover:bg-yellow-600 transition shadow-lg">Wyślij opinię</button>
          </div>
        </form>
      </div>
    </div>

    <navbar></navbar>

    <main class="max-w-7xl mx-auto p-8 mt-10">
      <header class="mb-10">
        <h2 class="text-3xl font-bold text-gray-800">Twoja kolekcja 📚</h2>
      </header>

      <div class="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
        <table class="w-full text-left">
          <thead class="bg-gray-50 border-b border-gray-100">
            <tr>
              <th class="px-6 py-4 text-sm font-semibold text-gray-600">Gra</th>
              <th class="px-6 py-4 text-sm font-semibold text-gray-600">Status</th>
              <th class="px-6 py-4 text-sm font-semibold text-gray-600 text-right">Akcje</th> 
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-100">
            <tr v-for="rental in rentals" :key="rental.id" class="hover:bg-gray-50 transition">
              <td class="px-6 py-4 font-bold text-gray-800">
                {{ rental.gameTitle }} <br>
                <span class="text-xs font-normal text-gray-500">Zwrot do: {{ rental.dueDate }}</span>
              </td>
              <td class="px-6 py-4">
                <span :class="{
                  'bg-emerald-100 text-emerald-700': rental.status === 'ACTIVE',
                  'bg-gray-100 text-gray-700': rental.status === 'RETURNED',
                  'bg-red-100 text-red-700': rental.status === 'LATE'
                }" class="px-3 py-1 rounded-full text-xs font-bold uppercase">
                  {{ rental.status }}
                </span>
              </td>
              <td class="px-6 py-4 text-right">
                <button 
                  v-if="rental.status === 'RETURNED'"
                  @click="openReviewModal(rental)"
                  class="px-4 py-2 bg-yellow-100 text-yellow-700 text-xs font-bold rounded-lg hover:bg-yellow-200 transition"
                >
                  ⭐ Oceń grę
                </button>

              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </main>
  </div>
</template>