<script setup lang="ts">
import { ref, onMounted } from 'vue';
import api from '../api/axios';
import Navbar from '../components/Navbar.vue';

const profileForm = ref({
  firstName: '',
  lastName: '',
  phoneNumbers: [] as string[],
  email: ''
});

const myHistory = ref<any[]>([]);
const notification = ref({ isOpen: false, message: '', isSuccess: true });

const fetchProfile = async () => {
  try {
    const response = await api.get('/clients/me');
    profileForm.value = {
      firstName: response.data.firstName || '',
      lastName: response.data.lastName || '',
      phoneNumbers: response.data.phoneNumbers || [],
      email: response.data.email || ''
    };
    if (profileForm.value.phoneNumbers.length === 0) {
      profileForm.value.phoneNumbers.push('');
    }
  } catch (error) { console.error("Błąd pobierania profilu"); }
};

const addPhoneField = () => {
  profileForm.value.phoneNumbers.push('');
};

const removePhoneField = (index: number) => {
  profileForm.value.phoneNumbers.splice(index, 1);
};

const saveProfile = async () => {
  try {
    const response = await api.put('/clients/me', profileForm.value);
    notification.value = { isOpen: true, message: response.data, isSuccess: true };
  } catch (error) {
    notification.value = { isOpen: true, message: "Błąd zapisu profilu", isSuccess: false };
  }
};

const fetchHistory = async () => {
  try {
    const response = await api.get('/rentals/my');
    myHistory.value = response.data;
  } catch (error) { console.error("Błąd pobierania historii"); }
};

onMounted(() => {
  fetchProfile();
  fetchHistory();
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

    <Navbar />

    <main class="max-w-6xl mx-auto p-8 mt-10 grid grid-cols-1 md:grid-cols-3 gap-8">
      
      <div class="md:col-span-1">
        <h2 class="text-3xl font-extrabold text-gray-900 mb-6">Twój Profil 👤</h2>
        <div class="bg-white rounded-3xl shadow-sm border border-gray-100 p-8">
          <form @submit.prevent="saveProfile" class="space-y-4">
            <div>
              <label class="block text-sm font-bold text-gray-700 mb-2">Adres E-mail (Login)</label>
              <input type="email" v-model="profileForm.email" disabled class="w-full px-4 py-3 bg-gray-100 border border-gray-200 rounded-xl text-gray-500 cursor-not-allowed">
            </div>
            <div>
              <label class="block text-sm font-bold text-gray-700 mb-2">Imię</label>
              <input type="text" v-model="profileForm.firstName" placeholder="Wpisz swoje imię" class="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl outline-none focus:ring-2 focus:ring-emerald-500">
            </div>
            <div>
              <label class="block text-sm font-bold text-gray-700 mb-2">Nazwisko</label>
              <input type="text" v-model="profileForm.lastName" placeholder="Wpisz swoje nazwisko" class="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl outline-none focus:ring-2 focus:ring-emerald-500">
            </div>
            <div class="pt-2 border-t border-gray-100 mt-4">
              <div class="flex justify-between items-center mb-2">
                <label class="block text-sm font-bold text-gray-700">Numery Telefonu</label>
                <button type="button" @click="addPhoneField" class="text-xs font-bold text-emerald-600 hover:text-emerald-700 bg-emerald-50 px-2 py-1 rounded-md transition">+ Dodaj kolejny</button>
              </div>
              
              <div v-for="(phone, index) in profileForm.phoneNumbers" :key="index" class="flex gap-2 mb-2">
                <input type="tel" v-model="profileForm.phoneNumbers[index]" placeholder="np. +48 123 456 789" class="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl outline-none focus:ring-2 focus:ring-emerald-500">
                <button type="button" v-if="profileForm.phoneNumbers.length > 1" @click="removePhoneField(index)" class="px-4 py-3 bg-red-50 text-red-500 rounded-xl hover:bg-red-100 transition" title="Usuń ten numer">
                  ❌
                </button>
              </div>
            </div>
            <button type="submit" class="w-full mt-4 py-3 bg-emerald-600 text-white font-bold rounded-xl hover:bg-emerald-700 transition shadow-md">Zapisz Zmiany</button>
          </form>
        </div>
      </div>

      <div class="md:col-span-2">
        <h2 class="text-3xl font-extrabold text-gray-900 mb-6">Historia Wypożyczeń 📚</h2>
        <div class="bg-white rounded-3xl shadow-sm border border-gray-100 overflow-hidden">
          <table class="w-full text-left">
            <thead class="bg-gray-50 border-b border-gray-100">
              <tr>
                <th class="px-6 py-4 text-sm font-semibold text-gray-600">Gra</th>
                <th class="px-6 py-4 text-sm font-semibold text-gray-600">Data wypożyczenia</th>
                <th class="px-6 py-4 text-sm font-semibold text-gray-600">Status</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-gray-100">
              <tr v-for="rental in myHistory" :key="rental.id" class="hover:bg-gray-50 transition">
                <td class="px-6 py-4 font-bold text-gray-800">{{ rental.gameTitle }}</td>
                <td class="px-6 py-4 text-gray-600 font-mono text-sm">{{ rental.rentalDate }}</td>
                <td class="px-6 py-4">
                  <span :class="{
                    'bg-emerald-100 text-emerald-700': rental.status === 'ACTIVE',
                    'bg-gray-100 text-gray-700': rental.status === 'RETURNED',
                    'bg-red-100 text-red-700': rental.status === 'LATE'
                  }" class="px-3 py-1 rounded-full text-xs font-bold uppercase">
                    {{ rental.status }}
                  </span>
                </td>
              </tr>
              <tr v-if="myHistory.length === 0">
                <td colspan="3" class="px-6 py-10 text-center text-gray-500 italic">Nie masz jeszcze żadnej historii wypożyczeń.</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

    </main>
  </div>
</template>