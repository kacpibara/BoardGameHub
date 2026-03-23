<script setup lang="ts">
import { ref } from 'vue';
import api from '../api/axios';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/authStore';

const router = useRouter();
const authStore = useAuthStore();

const form = ref({
  title: '',
  category: 'STRATEGICZNA', // Ustawiam wartość domyślną!
  minPlayers: 1,
  maxPlayers: 4,
  rentalPrice: 15.0,
  totalCopies: 1,
  imageUrl: ''
});

const submitGame = async () => {
  try {
    const response = await api.post('/games', form.value);
    alert("Sukces! " + response.data);
    router.push('/'); // Wracamy do katalogu, żeby zobaczyć nową grę
  } catch (error: any) {
    alert("Błąd: Brak uprawnień lub zły format danych! (403 = Nie jesteś Adminem)");
  }
};
</script>

<template>
  <div class="min-h-screen bg-gray-50 pb-20">
    <nav class="bg-gray-900 shadow-sm px-6 py-4 flex justify-between items-center">
      <h1 class="text-2xl font-black text-white tracking-tight cursor-pointer" @click="router.push('/')">BoardGameHub <span class="text-emerald-500">ADMIN</span></h1>
      <button @click="router.push('/')" class="text-gray-300 hover:text-white font-medium transition">Wróć do strony głównej</button>
    </nav>

    <main class="max-w-2xl mx-auto p-8 mt-10">
      <header class="mb-10 text-center">
        <h2 class="text-3xl font-extrabold text-gray-900">Dodaj nową grę 📦</h2>
      </header>

      <div class="bg-white rounded-3xl shadow-xl border border-gray-100 p-8">
        <form @submit.prevent="submitGame" class="space-y-6">
          
          <div>
            <label class="block text-sm font-bold text-gray-700 mb-2">Tytuł gry</label>
            <input type="text" v-model="form.title" required class="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl outline-none focus:ring-2 focus:ring-emerald-500">
          </div>

          <div class="grid grid-cols-2 gap-6">
            <div>
              <label class="block text-sm font-bold text-gray-700 mb-2">Kategoria</label>
              <select v-model="form.category" required class="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl outline-none focus:ring-2 focus:ring-emerald-500">
                <option value="STRATEGICZNA">Strategiczna</option>
                <option value="IMPREZOWA">Imprezowa</option>
                <option value="RODZINNA">Rodzinna</option>
                <option value="EKONOMICZNA">Ekonomiczna</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-bold text-gray-700 mb-2">URL Okładki</label>
              <input type="url" v-model="form.imageUrl" placeholder="https://..." class="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl outline-none focus:ring-2 focus:ring-emerald-500">
            </div>
          </div>

          <div class="grid grid-cols-4 gap-4">
            <div>
              <label class="block text-xs font-bold text-gray-700 mb-2">Min Graczy</label>
              <input type="number" min="1" v-model="form.minPlayers" required class="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl">
            </div>
            <div>
              <label class="block text-xs font-bold text-gray-700 mb-2">Max Graczy</label>
              <input type="number" min="1" v-model="form.maxPlayers" required class="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl">
            </div>
            <div>
              <label class="block text-xs font-bold text-gray-700 mb-2">Cena (zł)</label>
              <input type="number" step="0.01" v-model="form.rentalPrice" required class="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl">
            </div>
            <div>
              <label class="block text-xs font-bold text-gray-700 mb-2">Sztuki</label>
              <input type="number" min="1" v-model="form.totalCopies" required class="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl">
            </div>
          </div>

          <button type="submit" class="w-full mt-8 bg-emerald-600 text-white font-black text-lg py-4 rounded-xl hover:bg-emerald-700 transition shadow-lg">
            ➕ Dodaj do katalogu
          </button>
        </form>
      </div>
    </main>
  </div>
</template>