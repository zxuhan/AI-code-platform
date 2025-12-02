import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '@/pages/HomePage.vue'
import UserLoginPage from '@/pages/user/UserLoginPage.vue'
import UserRegisterPage from '@/pages/user/UserRegisterPage.vue'
import UserManagePage from '@/pages/admin/UserManagePage.vue'
import AppManagePage from '@/pages/admin/AppManagePage.vue'
import AppChatPage from '@/pages/app/AppChatPage.vue'
import AppEditPage from '@/pages/app/AppEditPage.vue'
import ChatManagePage from "@/pages/admin/ChatManagePage.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Home Page',
      component: HomePage,
    },
    {
      path: '/user/login',
      name: 'User Login',
      component: UserLoginPage,
    },
    {
      path: '/user/register',
      name: 'User Register',
      component: UserRegisterPage,
    },
    {
      path: '/admin/userManage',
      name: 'User Management',
      component: UserManagePage,
    },
    {
      path: '/admin/appManage',
      name: 'App Management',
      component: AppManagePage,
    },
    {
      path: '/admin/chatManage',
      name: 'Chat Management',
      component: ChatManagePage,
    },
    {
      path: '/app/chat/:id',
      name: 'App Chat',
      component: AppChatPage,
    },
    {
      path: '/app/edit/:id',
      name: 'App Edit',
      component: AppEditPage,
    },
  ],
})

export default router
