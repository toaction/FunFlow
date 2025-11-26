import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ProfileView from '../views/ProfileView.vue'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/profile',
      name: 'profile',
      component: ProfileView,
      meta: { requiresAuth: true }
    }
  ]
})

// 添加全局导航守卫
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()

  // 检查目标路由是否需要认证
  if (to.meta.requiresAuth) {
    if (!authStore.token) {
      // 未登录时，不进行重定向，让组件处理
      next()
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router

