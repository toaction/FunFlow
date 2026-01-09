<template>
  <header class="header">
    <div class="header-left">
      <img src="/images/logo.jpeg" alt="FunFlow" class="logo" />
      <span class="logo-text">FunFlow</span>
    </div>

    <div class="header-center">
      <div class="search-box">
        <input
          v-model="searchQuery"
          type="text"
          placeholder="搜索..."
          class="search-input"
          @keyup.enter="handleSearch"
        />
        <button class="search-button" @click="handleSearch">
          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="11" cy="11" r="8"></circle>
            <path d="m21 21-4.35-4.35"></path>
          </svg>
        </button>
      </div>
    </div>

    <div class="header-right">
      <button class="login-btn" @click="handleLogin">
        登录
      </button>
    </div>
  </header>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const searchQuery = ref('')

const handleSearch = () => {
  if (searchQuery.value.trim()) {
    console.log('搜索:', searchQuery.value)
    // TODO: 实现搜索逻辑
  }
}

const handleLogin = () => {
  console.log('点击登录按钮')
  // TODO: 实现登录逻辑
}
</script>

<style scoped>
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  height: 64px;
  background: #161823;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
  min-width: 200px;
}

.logo {
  height: 36px;
  width: auto;
  object-fit: contain;
  border-radius: 8px;
}

.logo-text {
  font-size: 20px;
  font-weight: 600;
  color: #ffffff;
  letter-spacing: 0.5px;
}

.header-center {
  flex: 1;
  display: flex;
  justify-content: center;
  max-width: 600px;
  margin: 0 24px;
}

.search-box {
  display: flex;
  align-items: center;
  width: 100%;
  max-width: 500px;
  background: #242634;
  border-radius: 18px;
  padding: 6px 6px 6px 14px;
  transition: all 0.2s;
}

.search-box:focus-within {
  background: #242634;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}

.search-input {
  flex: 1;
  border: none;
  background: transparent;
  outline: none;
  font-size: 14px;
  color: #ffffff;
}

.search-input::placeholder {
  color: #888888;
}

.search-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 30px;
  height: 30px;
  border: none;
  background: transparent;
  color: #888888;
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.2s;
}

.search-button:hover {
  background: rgba(24, 144, 255, 0.1);
  color: #1890ff;
}

.header-right {
  display: flex;
  align-items: center;
  min-width: 200px;
  justify-content: flex-end;
}

.login-btn {
  padding: 8px 24px;
  background: #1890ff;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.login-btn:hover {
  background: #40a9ff;
}
</style>
