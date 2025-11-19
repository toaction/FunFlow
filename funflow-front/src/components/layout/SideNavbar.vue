<script setup lang="ts">
import { RouterLink, useRoute } from 'vue-router'
import { ElScrollbar } from 'element-plus'

const route = useRoute()
</script>

<template>
  <aside class="sidebar">
    <el-scrollbar>
      <!-- 导航菜单 -->
      <nav class="menu">
        <div class="menu-section">
          <div class="section-title">主要功能</div>
          
          <!-- 热门视频 -->
          <RouterLink to="/hot" class="menu-item" active-class="active">
            <div class="item-content">
              <svg class="nav-icon" viewBox="0 0 24 24" fill="currentColor">
                <path d="M16 6l2.29 2.29-4.88 4.88-4-4L2 16.59 3.41 18l6-6 4 4 6.3-6.29L22 12V6z"/>
              </svg>
              <span class="item-text">热门视频</span>
            </div>
            <div class="active-indicator"></div>
          </RouterLink>

          <!-- 首页推荐 -->
          <RouterLink to="/" class="menu-item" active-class="active">
            <div class="item-content">
              <svg class="nav-icon" viewBox="0 0 24 24" fill="currentColor">
                <path d="M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z"/>
              </svg>
              <span class="item-text">首页推荐</span>
            </div>
            <div class="active-indicator"></div>
          </RouterLink>
        </div>

        <div class="menu-divider"></div>

        <div class="menu-section">
          <div class="section-title">个人空间</div>
          
          <!-- 我的关注 -->
          <RouterLink to="/follow" class="menu-item" active-class="active">
            <div class="item-content">
              <svg class="nav-icon" viewBox="0 0 24 24" fill="currentColor">
                <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
              </svg>
              <span class="item-text">我的关注</span>
            </div>
            <div class="active-indicator"></div>
          </RouterLink>

          <!-- 个人中心 -->
          <RouterLink to="/profile" class="menu-item" active-class="active">
            <div class="item-content">
              <svg class="nav-icon" viewBox="0 0 24 24" fill="currentColor">
                <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 3c1.66 0 3 1.34 3 3s-1.34 3-3 3-3-1.34-3-3 1.34-3 3-3zm0 14.2c-2.5 0-4.71-1.28-6-3.22.03-1.99 4-3.08 6-3.08 1.99 0 5.97 1.09 6 3.08-1.29 1.94-3.5 3.22-6 3.22z"/>
              </svg>
              <span class="item-text">个人中心</span>
            </div>
            <div class="active-indicator"></div>
          </RouterLink>
        </div>
      </nav>
    </el-scrollbar>
  </aside>
</template>

<style scoped>
.sidebar {
  width: 260px;
  background: linear-gradient(180deg, #1a1d29 0%, #161823 100%);
  height: calc(100vh - 64px);
  position: fixed;
  top: 64px;
  left: 0;
  z-index: 900;
  border-right: 1px solid rgba(255, 255, 255, 0.05);
  box-shadow: 2px 0 12px rgba(0, 0, 0, 0.3);
}

/* 菜单区域 */
.menu {
  padding: 20px 12px;
}

.menu-section {
  margin-bottom: 20px;
}

.section-title {
  font-size: 12px;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.4);
  text-transform: uppercase;
  letter-spacing: 1px;
  padding: 8px 16px;
  margin-bottom: 4px;
}

.menu-divider {
  height: 1px;
  background: linear-gradient(
    90deg,
    transparent 0%,
    rgba(255, 255, 255, 0.1) 50%,
    transparent 100%
  );
  margin: 16px 8px;
}

.menu-item {
  display: block;
  position: relative;
  text-decoration: none;
  margin-bottom: 6px;
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.item-content {
  display: flex;
  align-items: center;
  padding: 14px 16px;
  gap: 14px;
  position: relative;
  z-index: 1;
}

.nav-icon {
  flex-shrink: 0;
  width: 24px;
  height: 24px;
  color: rgba(255, 255, 255, 0.7);
  transition: all 0.3s ease;
}

.item-text {
  font-size: 16px;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.75);
  transition: all 0.3s ease;
}

.active-indicator {
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 0;
  background: linear-gradient(180deg, #fe2c55 0%, #ff6b6b 100%);
  border-radius: 0 4px 4px 0;
  transition: height 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 悬停效果 */
.menu-item:hover {
  background: rgba(255, 255, 255, 0.06);
  transform: translateX(4px);
}

.menu-item:hover .nav-icon {
  color: rgba(255, 255, 255, 0.95);
  transform: scale(1.1);
}

.menu-item:hover .item-text {
  color: rgba(255, 255, 255, 0.95);
}

/* 活跃状态 */
.menu-item.active {
  background: linear-gradient(
    90deg,
    rgba(254, 44, 85, 0.15) 0%,
    rgba(254, 44, 85, 0.05) 100%
  );
  box-shadow: inset 0 0 20px rgba(254, 44, 85, 0.1);
}

.menu-item.active .nav-icon {
  color: #fe2c55;
  transform: scale(1.05);
}

.menu-item.active .item-text {
  color: #fe2c55;
  font-weight: 700;
}

.menu-item.active .active-indicator {
  height: 32px;
  box-shadow: 0 0 12px rgba(254, 44, 85, 0.6);
}

.menu-item.active:hover {
  background: linear-gradient(
    90deg,
    rgba(254, 44, 85, 0.2) 0%,
    rgba(254, 44, 85, 0.08) 100%
  );
  transform: translateX(4px);
}

/* 动画效果 */
@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateX(-20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.menu-item {
  animation: slideIn 0.3s ease forwards;
}

.menu-item:nth-child(1) {
  animation-delay: 0.05s;
}

.menu-item:nth-child(2) {
  animation-delay: 0.1s;
}

.menu-item:nth-child(3) {
  animation-delay: 0.15s;
}

.menu-item:nth-child(4) {
  animation-delay: 0.2s;
}

/* 滚动条样式 */
:deep(.el-scrollbar__wrap) {
  overflow-x: hidden;
}

:deep(.el-scrollbar__bar.is-vertical) {
  width: 4px;
  right: 2px;
}

:deep(.el-scrollbar__thumb) {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 4px;
}

:deep(.el-scrollbar__thumb:hover) {
  background: rgba(255, 255, 255, 0.3);
}
</style>
