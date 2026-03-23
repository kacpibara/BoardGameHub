import { createRouter, createWebHistory } from 'vue-router'

// Na razie importujemy "na sucho", zaraz stworzymy te pliki!
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/HomeView.vue')
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue')
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/RegisterView.vue')
    },
    {
      path: '/my-rentals',
      name: 'my-rentals',
      component: () => import('../views/MyRentalsView.vue')
    },
    {
      path: '/reserve',
      name: 'reserve',
      component: () => import('../views/TableReservationView.vue')
    },
    {
      path: '/admin',
      name: 'admin',
      component: () => import('../views/AdminView.vue')
    }
  
  ]
})

router.beforeEach((to, from) => { // usuwamy trzeci argument 'next'
  const publicPages = ['/login', '/register'];
  const authRequired = !publicPages.includes(to.path);
  const loggedIn = localStorage.getItem('token');

  if (authRequired && !loggedIn) {
    return '/login'; // zwracamy ścieżkę zamiast next('/login')
  }

  if (loggedIn && publicPages.includes(to.path)) {
    return '/'; // zwracamy ścieżkę zamiast next('/')
  }
});

export default router