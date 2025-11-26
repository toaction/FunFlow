<template>
  <div class="profile-page">
    <!-- 个人中心内容区域 -->
    <main class="profile-container">
      <!-- 用户信息卡片 -->
      <section class="user-info-card">
        <div class="user-header">
          <div class="user-avatar-large">
            <svg viewBox="0 0 24 24" fill="white">
              <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
            </svg>
          </div>
          <div class="user-basic-info">
            <div class="user-nickname-wrapper">
              <h1 class="user-nickname">FunFlow</h1>
              <button class="edit-profile-btn" title="编辑个人资料" @click="editProfile">
                <svg viewBox="0 0 24 24" fill="currentColor">
                  <path d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z"/>
                </svg>
              </button>
            </div>
            <div class="user-account">账号：<span>funflow2025</span></div>
            <div class="user-stats">
              <div class="stat-item" @click="viewFollowing">
                <div class="stat-number">{{ userInfo.following }}</div>
                <div class="stat-label">关注</div>
              </div>
              <div class="stat-item" @click="viewFollowers">
                <div class="stat-number">{{ userInfo.followers }}</div>
                <div class="stat-label">粉丝</div>
              </div>
              <div class="stat-item" @click="viewLikes">
                <div class="stat-number">{{ userInfo.totalLikes }}</div>
                <div class="stat-label">获赞</div>
              </div>
            </div>
            <div class="user-bio">
              <div class="bio-content">{{ userInfo.bio }}</div>
            </div>
          </div>
        </div>
      </section>

      <!-- 作品导航标签 -->
      <section class="works-tabs-container">
        <div class="works-tabs">
          <button
            class="works-tab"
            :class="{ active: activeTab === 'works' }"
            @click="switchTab('works')"
          >
            作品 <span class="works-tab-count">({{ userWorks.works.length }})</span>
          </button>
          <button
            class="works-tab"
            :class="{ active: activeTab === 'likes' }"
            @click="switchTab('likes')"
          >
            喜欢 <span class="works-tab-count">({{ userWorks.likes.length }})</span>
          </button>
          <button
            class="works-tab"
            :class="{ active: activeTab === 'favorites' }"
            @click="switchTab('favorites')"
          >
            收藏 <span class="works-tab-count">({{ userWorks.favorites.length }})</span>
          </button>
          <button
            class="works-tab"
            :class="{ active: activeTab === 'history' }"
            @click="switchTab('history')"
          >
            观看历史 <span class="works-tab-count">({{ userWorks.history.length }})</span>
          </button>
        </div>

        <div class="works-content">
          <!-- 作品内容区域 -->
          <div class="works-content-section" :class="{ active: activeTab === 'works' }">
            <div class="works-grid">
              <div
                v-for="(work, index) in userWorks.works"
                :key="`work-${index}`"
                class="work-item"
                @click="playVideo(work)"
              >
                <div class="work-placeholder">{{ work.icon }}</div>
                <div class="work-info">
                  <div class="work-stats">
                    <div class="work-stat">
                      <span>❤️</span>
                      <span>{{ formatCount(work.likes) }}</span>
                    </div>
                    <div class="work-stat">
                      <span>💬</span>
                      <span>{{ formatCount(work.comments) }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 喜欢内容区域 -->
          <div class="works-content-section" :class="{ active: activeTab === 'likes' }">
            <div class="works-grid">
              <div
                v-for="(like, index) in userWorks.likes"
                :key="`like-${index}`"
                class="work-item"
                @click="playVideo(like)"
              >
                <div class="work-placeholder">{{ like.icon }}</div>
                <div class="work-info">
                  <div class="work-stats">
                    <div class="work-stat">
                      <span>❤️</span>
                      <span>{{ formatCount(like.likes) }}</span>
                    </div>
                    <div class="work-stat">
                      <span>💬</span>
                      <span>{{ formatCount(like.comments) }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 收藏文件夹列表 -->
          <div class="works-content-section" :class="{ active: activeTab === 'favorites' }">
            <div class="folders-list">
              <div
                v-for="(folder, index) in userWorks.favorites"
                :key="`folder-${index}`"
                class="folder-item"
                @click="openFolder(folder)"
              >
                <div class="folder-icon">{{ folder.icon }}</div>
                <div class="folder-info">
                  <div class="folder-name">{{ folder.name }}</div>
                  <div class="folder-count">{{ folder.count }}个视频</div>
                </div>
              </div>
            </div>
          </div>

          <!-- 观看历史区域 -->
          <div class="works-content-section" :class="{ active: activeTab === 'history' }">
            <div class="works-grid">
              <div
                v-for="(history, index) in userWorks.history"
                :key="`history-${index}`"
                class="work-item"
                @click="playVideo(history)"
              >
                <div class="work-placeholder">{{ history.icon }}</div>
                <div class="work-info">
                  <div class="work-stats">
                    <div class="work-stat">
                      <span>❤️</span>
                      <span>{{ formatCount(history.likes) }}</span>
                    </div>
                    <div class="work-stat">
                      <span>💬</span>
                      <span>{{ formatCount(history.comments) }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
const activeTab = ref('works')

// 用户信息
const userInfo = reactive({
  following: 128,
  followers: 2500,
  totalLikes: 15800,
  bio: '热爱生活，分享快乐 ✨ 记录生活中的美好瞬间 📸 欢迎关注交流 💫'
})

// 用户作品数据
const userWorks = reactive({
  works: [
    { id: 1, icon: '📹', likes: 1200, comments: 56 },
    { id: 2, icon: '🎬', likes: 890, comments: 42 },
    { id: 3, icon: '🎥', likes: 2300, comments: 78 },
    { id: 4, icon: '📽️', likes: 1500, comments: 63 },
    { id: 5, icon: '🎞️', likes: 980, comments: 34 },
    { id: 6, icon: '📹', likes: 1800, comments: 92 }
  ],
  likes: [
    { id: 101, icon: '❤️', likes: 3200, comments: 125 },
    { id: 102, icon: '💖', likes: 2800, comments: 98 }
  ],
  favorites: [
    { id: 201, name: '默认收藏夹', icon: '📁', count: 45 },
    { id: 202, name: '搞笑视频', icon: '🎬', count: 28 },
    { id: 203, name: '音乐舞蹈', icon: '🎵', count: 16 },
    { id: 204, name: '美食教程', icon: '🍲', count: 32 },
    { id: 205, name: '旅行vlog', icon: '✈️', count: 21 },
    { id: 206, name: '生活技巧', icon: '💡', count: 19 }
  ],
  history: [
    { id: 301, icon: '🕐', likes: 5600, comments: 234 },
    { id: 302, icon: '⏰', likes: 4200, comments: 156 }
  ]
})

// 方法
const switchTab = (tab: string) => {
  activeTab.value = tab
  console.log('切换到标签:', tab)
}

const editProfile = () => {
  console.log('编辑个人资料')
  // 这里可以添加打开编辑个人资料弹窗的逻辑
}

const viewFollowing = () => {
  console.log('查看关注列表')
  // 这里可以添加跳转到关注列表的逻辑
}

const viewFollowers = () => {
  console.log('查看粉丝列表')
  // 这里可以添加跳转到粉丝列表的逻辑
}

const viewLikes = () => {
  console.log('查看获赞列表')
  // 这里可以添加跳转到获赞列表的逻辑
}

const playVideo = (video: any) => {
  console.log('播放视频:', video)
  // 这里可以添加播放视频或跳转到详情页的逻辑
}

const openFolder = (folder: any) => {
  console.log('打开收藏夹:', folder)
  // 这里可以添加打开文件夹查看内容的逻辑
}

const formatCount = (count: number): string => {
  if (count >= 10000) {
    return (count / 1000).toFixed(1) + 'K'
  } else if (count >= 1000) {
    return (count / 1000).toFixed(1) + 'K'
  }
  return count.toString()
}
</script>

<style scoped>
.profile-page {
  width: 100%;
  background: transparent;
}

/* 个人中心内容区域 */
.profile-container {
  background: transparent;
  overflow-y: auto;
  padding: 20px 40px 20px 20px;
  min-height: calc(100vh - 64px);
}

/* 自定义滚动条 */
.profile-container::-webkit-scrollbar {
  width: 8px;
}

.profile-container::-webkit-scrollbar-track {
  background: #1a1a1a;
}

.profile-container::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 4px;
}

.profile-container::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.3);
}

/* 用户信息卡片 */
.user-info-card {
  background: #161823;
  border-radius: 16px;
  padding: 30px;
  margin-bottom: 30px;
  border: 1px solid #2b2d38;
}

.user-header {
  display: flex;
  align-items: flex-start;
  gap: 25px;
}

.user-avatar-large {
  width: 140px;
  height: 140px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  border: 3px solid #2b2d38;
  overflow: hidden;
  flex-shrink: 0;
}

.user-avatar-large svg {
  width: 70px;
  height: 70px;
}

.user-avatar-large img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-basic-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  padding-top: 10px;
  min-width: 0;
}

.user-nickname-wrapper {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 6px;
}

.user-nickname {
  font-size: 24px;
  font-weight: 700;
  color: #fff;
  margin: 0;
}

.edit-profile-btn {
  width: 20px;
  height: 20px;
  cursor: pointer;
  opacity: 0.7;
  transition: opacity 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
  background: none;
  border: none;
  padding: 0;
}

.edit-profile-btn:hover {
  opacity: 1;
}

.edit-profile-btn svg {
  width: 16px;
  height: 16px;
  fill: #aaa;
}

.user-account {
  font-size: 14px;
  color: #aaa;
  margin-bottom: 15px;
}

.user-account span {
  color: #999;
  font-weight: 500;
}

.user-stats {
  display: flex;
  gap: 40px;
  margin-bottom: 15px;
}

.stat-item {
  display: flex;
  align-items: center;
  cursor: pointer;
  transition: transform 0.2s;
  gap: 6px;
}

.stat-item:hover {
  transform: translateY(-2px);
}

.stat-number {
  font-size: 20px;
  font-weight: 700;
  color: #fff;
}

.stat-label {
  font-size: 13px;
  color: #888;
}

.user-bio {
  background: transparent;
  padding: 0;
  border-radius: 0;
  margin: 0;
}

.bio-content {
  font-size: 14px;
  color: #ddd;
  line-height: 1.5;
}

/* 作品导航标签 */
.works-tabs-container {
  background: #161823;
  border-radius: 16px;
  padding: 0;
  border: 1px solid #2b2d38;
  overflow: hidden;
}

.works-tabs {
  display: flex;
  border-bottom: 1px solid #2b2d38;
}

.works-tab {
  flex: 1;
  padding: 20px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 16px;
  font-weight: 500;
  color: #888;
  position: relative;
  border-bottom: 3px solid transparent;
  background: none;
  border: none;
  font-family: inherit;
}

.works-tab:hover {
  color: #ddd;
  background: rgba(255, 255, 255, 0.03);
}

.works-tab.active {
  color: #fe2c55;
  background: rgba(254, 44, 85, 0.05);
  border-bottom-color: #fe2c55;
}

.works-tab-count {
  font-size: 13px;
  color: #666;
  margin-left: 5px;
}

.works-tab.active .works-tab-count {
  color: #fe2c55;
}

/* 作品内容区域 */
.works-content {
  padding: 30px;
  min-height: 400px;
}

.works-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.work-item {
  aspect-ratio: 9/16;
  background: #1a1a1a;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  position: relative;
  transition: transform 0.3s;
  border: 1px solid #2b2d38;
}

.work-item:hover {
  transform: translateY(-5px);
  border-color: #fe2c55;
}

.work-item::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, transparent 0%, rgba(0,0,0,0.7) 100%);
  z-index: 1;
}

.work-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
  background: #2a2a2a;
}

.work-info {
  position: absolute;
  bottom: 10px;
  left: 10px;
  right: 10px;
  z-index: 2;
  color: white;
}

.work-stats {
  display: flex;
  gap: 10px;
  font-size: 12px;
}

.work-stat {
  display: flex;
  align-items: center;
  gap: 4px;
}

/* 收藏文件夹列表样式 */
.folders-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.folder-item {
  background: #1a1a1a;
  border: 1px solid #2b2d38;
  border-radius: 12px;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  gap: 16px;
}

.folder-item:hover {
  background: #222;
  border-color: #fe2c55;
  transform: translateY(-3px);
}

.folder-icon {
  width: 56px;
  height: 56px;
  background: rgba(254, 44, 85, 0.1);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  flex-shrink: 0;
}

.folder-info {
  flex: 1;
  min-width: 0;
}

.folder-name {
  font-size: 16px;
  font-weight: 600;
  color: #fff;
  margin-bottom: 6px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.folder-count {
  font-size: 13px;
  color: #888;
}

.folder-item:hover .folder-name {
  color: #fe2c55;
}

/* 隐藏/显示内容 */
.works-content-section {
  display: none;
}

.works-content-section.active {
  display: block;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .sidebar {
    width: 160px;
  }

  .nav-item {
    padding: 12px 20px;
    font-size: 14px;
  }

  .profile-container {
    padding: 30px 40px;
  }

  .user-header {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .user-basic-info {
    align-items: center;
  }

  .works-grid {
    grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
  }
}

@media (max-width: 768px) {
  .main-wrapper {
    flex-direction: column;
  }

  .sidebar {
    width: 100%;
    height: auto;
    border-right: none;
    border-bottom: 1px solid #2b2d38;
    flex-direction: row;
    overflow-x: auto;
  }

  .profile-container {
    padding: 20px;
  }

  .search-section {
    margin: 0 20px;
  }

  .logo-section span {
    font-size: 20px;
  }

  .works-tabs {
    overflow-x: auto;
  }

  .works-tab {
    white-space: nowrap;
    padding: 15px 20px;
    font-size: 14px;
  }

  .works-grid {
    grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
    gap: 12px;
  }

  .folders-list {
    grid-template-columns: 1fr;
  }
}
</style>