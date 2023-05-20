import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user_store';
import HomeVue from '@/views/Home.vue';
import LoginVue from '@/views/auth/Login.vue';
import NotFoundVue from '@/views/NotFound.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeVue,
      beforeEnter: (to, from) => {if (!isUserAuth()) { return { name: 'login' }; }}
    },
    {
      path: '/login',
      name: 'login',
      component: LoginVue,
      beforeEnter: (to, from) => {if (isUserAuth()) { return { name: 'home' }; }}
    },
    { 
      path: '/:pathMatch(.*)*', 
      component: NotFoundVue 
    }
  ]
})

const isUserAuth = (): boolean => {
  const userStore = useUserStore();
  return userStore.isUserLoggedIn;
}

export default router
