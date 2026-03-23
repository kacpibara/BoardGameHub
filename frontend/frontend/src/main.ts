import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
//import './style.css' 

const app = createApp(App)

app.use(createPinia()) // Odpalamy globalny stan (Pinia)
app.use(router)        // Odpalamy nawigację (Router)

app.mount('#app')