import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user_store';
import Home from '@/views/Home.vue';
import Login from '@/views/auth/Login.vue';
import NotFound from '@/views/NotFound.vue';
import Logout from '@/views/auth/Logout.vue';
import ForgetPassword from '@/views/auth/Forget-password.vue';
import ResetPassword from '@/views/auth/Reset-password.vue';
import NewUser from '@/views/auth/New-User.vue';
import UserActivation from '@/views/auth/User-activation.vue';
import UserActivationRequest from '@/views/auth/User-activation-request.vue';
import PasswordUpdate from '@/views/user/PasswordUpdate.vue';
import ProfileUpdate from '@/views/user/ProfileUpdate.vue';
import TaskView from '@/views/task/TaskView.vue';
import ListEdit from '@/views/task/ListEdit.vue';
import TaskEdit from '@/views/task/TaskEdit.vue';


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'index',
      component: Home,
      beforeEnter: (to, from) => { return { name: 'taskview' } },
    },
    {
      path: '/home',
      name: 'home',
      component: Home,
      children: [
        {
          path: '',
          name: 'taskview',
          component: TaskView,
          beforeEnter: (to, from) => { if (!isUserAuth()) { return { name: 'login' }; } },
        },
        {
          path: 'list/:id',
          name: 'editlist',
          component: ListEdit,
          beforeEnter: (to, from) => { if (!isUserAuth()) { return { name: 'login' }; } },
        },
        {
          path: 'task/:id',
          name: 'edittask',
          component: TaskEdit,
          beforeEnter: (to, from) => { if (!isUserAuth()) { return { name: 'login' }; } },
        },

        {
          path: 'mypassword',
          name: 'mypassword',
          component: PasswordUpdate,
          beforeEnter: (to, from) => { if (!isUserAuth()) { return { name: 'login' }; } },
        },
        {
          path: 'profile',
          name: 'profile',
          component: ProfileUpdate,
          beforeEnter: (to, from) => { if (!isUserAuth()) { return { name: 'login' }; } },
        },
      ],
      beforeEnter: (to, from) => { if (!isUserAuth()) { return { name: 'login' }; } },

    },
    {
      path: '/login',
      name: 'login',
      component: Login,
      beforeEnter: (to, from) => { if (isUserAuth()) { return { name: 'taskview' }; } }
    },
    {
      path: '/recoverpassword',
      name: 'recoverpassword',
      component: ForgetPassword,
      beforeEnter: (to, from) => { if (isUserAuth()) { return { name: 'taskview' }; } }
    },
    {
      path: '/passwordreset/:email',
      name: 'passwordreset',
      component: ResetPassword,
      beforeEnter: (to, from) => { if (isUserAuth()) { return { name: 'taskview' }; } }
    },
    {
      path: '/userregistration',
      name: 'userregistration',
      component: NewUser,
      beforeEnter: (to, from) => { if (isUserAuth()) { return { name: 'taskview' }; } }
    },
    {
      path: '/useractivation/:email',
      name: 'useractivation',
      component: UserActivation,
      beforeEnter: (to, from) => { if (isUserAuth()) { return { name: 'taskview' }; } }
    },
    {
      path: '/useractivationrequest/:email',
      name: 'useractivationrequest',
      component: UserActivationRequest,
      beforeEnter: (to, from) => { if (isUserAuth()) { return { name: 'taskview' }; } }
    },
    {
      path: '/logout',
      name: 'logout',
      component: Logout,
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      component: NotFound
    }
  ]
})

const isUserAuth = (): boolean => {
  const userStore = useUserStore();
  return userStore.isUserLoggedIn;
}

export default router
