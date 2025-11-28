import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ProfileView from '../views/ProfileView.vue'
import CreateView from '../views/CreateView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/hot',
      name: 'hot',
      component: HomeView
    },
    {
      path: '/follow',
      name: 'follow',
      component: HomeView
    },
    {
      path: '/profile',
      name: 'profile',
      component: ProfileView,
      meta: { requiresAuth: true }
    },
    {
      path: '/create',
      name: 'create',
      component: CreateView,
      meta: { requiresAuth: true }
    }
  ]
})

export default router

