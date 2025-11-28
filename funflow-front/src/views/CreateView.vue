<template>
  <div class="create-page">
    <!-- 创作页面头部 -->
    <header class="create-header">
      <h1 class="create-title">视频创作</h1>
      <div class="header-actions">
        <button class="draft-btn" @click="saveDraft" :disabled="isUploading">
          <svg viewBox="0 0 24 24" fill="currentColor">
            <path d="M17 3H5c-1.11 0-2 .9-2 2v14c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V7l-4-4zm-5 16c-1.66 0-3-1.34-3-3s1.34-3 3-3 3 1.34 3 3-1.34 3-3 3zm3-10H5V5h10v4z"/>
          </svg>
          存为草稿
        </button>
        <button class="publish-btn" @click="publishVideo" :disabled="isUploading || !canPublish">
          <svg viewBox="0 0 24 24" fill="currentColor">
            <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
          </svg>
          发布视频
        </button>
      </div>
    </header>

    <!-- 创作内容区域 -->
    <main class="create-container">
      <!-- 左侧：上传区域 -->
      <section class="upload-section">
        <!-- 视频上传区 -->
        <div class="upload-card">
          <h3 class="card-title">视频上传</h3>
          <div
            class="video-upload-zone"
            :class="{ 'has-video': videoFile, 'dragover': isDragover }"
            @drop="handleDrop"
            @dragover.prevent="isDragover = true"
            @dragleave="isDragover = false"
            @click="triggerVideoUpload"
          >
            <input
              ref="videoInput"
              type="file"
              accept="video/*"
              @change="handleVideoSelect"
              style="display: none"
            />

            <!-- 上传中状态 -->
            <div v-if="isUploading" class="uploading-state">
              <div class="loading-spinner"></div>
              <p class="upload-progress">{{ uploadProgress }}%</p>
              <p class="upload-text">正在上传视频...</p>
            </div>

            <!-- 已上传视频预览 -->
            <div v-else-if="videoPreview" class="video-preview">
              <video :src="videoPreview" controls class="preview-video"></video>
              <button class="remove-video-btn" @click="removeVideo">
                <svg viewBox="0 0 24 24" fill="currentColor">
                  <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
                </svg>
              </button>
            </div>

            <!-- 默认上传提示 -->
            <div v-else class="upload-placeholder">
              <svg class="upload-icon" viewBox="0 0 24 24" fill="currentColor">
                <path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/>
              </svg>
              <p class="upload-title">点击或拖拽视频文件到此处</p>
              <p class="upload-hint">支持 MP4、AVI、MOV 等格式，文件大小不超过 2GB</p>
            </div>
          </div>
        </div>

        <!-- 封面上传区 -->
        <div class="upload-card">
          <h3 class="card-title">视频封面</h3>
          <div
            class="cover-upload-zone"
            :class="{ 'has-cover': coverFile }"
            @click="triggerCoverUpload"
          >
            <input
              ref="coverInput"
              type="file"
              accept="image/*"
              @change="handleCoverSelect"
              style="display: none"
            />

            <!-- 已上传封面预览 -->
            <div v-if="coverPreview" class="cover-preview">
              <img :src="coverPreview" alt="视频封面" class="preview-cover">
              <button class="remove-cover-btn" @click="removeCover">
                <svg viewBox="0 0 24 24" fill="currentColor">
                  <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
                </svg>
              </button>
              <!-- 自动生成封面按钮 -->
              <button class="auto-generate-btn" @click="autoGenerateCover" :disabled="!videoFile">
                <svg viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
                </svg>
                自动生成
              </button>
            </div>

            <!-- 默认上传提示 -->
            <div v-else class="upload-placeholder">
              <svg class="upload-icon" viewBox="0 0 24 24" fill="currentColor">
                <path d="M21 19V5c0-1.1-.9-2-2-2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2zM8.5 13.5l2.5 3.01L14.5 12l4.5 6H5l3.5-4.5z"/>
              </svg>
              <p class="upload-title">点击上传封面</p>
              <p class="upload-hint">建议尺寸：1080×1920，支持 JPG、PNG 格式</p>
            </div>
          </div>
        </div>
      </section>

      <!-- 右侧：信息填写区 -->
      <section class="info-section">
        <!-- 文案输入 -->
        <div class="info-card">
          <h3 class="card-title">视频文案</h3>
          <textarea
            v-model="videoForm.description"
            class="description-input"
            placeholder="写一段有趣的文案，让大家更了解你的视频..."
            maxlength="500"
            rows="4"
          ></textarea>
          <div class="char-count">{{ videoForm.description.length }}/500</div>
        </div>

        <!-- 标签设置 -->
        <div class="info-card">
          <h3 class="card-title">添加标签</h3>
          <div class="tag-input-wrapper">
            <input
              v-model="tagInput"
              class="tag-input"
              placeholder="输入标签后按回车添加"
              @keyup.enter="addTag"
              maxlength="20"
            />
            <button class="add-tag-btn" @click="addTag">添加</button>
          </div>
          <div class="tag-list">
            <div
              v-for="(tag, index) in videoForm.tags"
              :key="index"
              class="tag-item"
            >
              {{ tag }}
              <button class="remove-tag-btn" @click="removeTag(index)">
                ×
              </button>
            </div>
          </div>
          <!-- 推荐标签 -->
          <div class="suggested-tags">
            <span class="suggested-title">推荐标签：</span>
            <button
              v-for="tag in suggestedTags"
              :key="tag"
              class="suggested-tag"
              @click="addSuggestedTag(tag)"
            >
              {{ tag }}
            </button>
          </div>
        </div>

        <!-- 发布设置 -->
        <div class="info-card">
          <h3 class="card-title">发布设置</h3>
          <div class="setting-item">
            <label class="setting-label">
              <input
                type="radio"
                v-model="videoForm.privacy"
                value="public"
                name="privacy"
              />
              <span class="radio-custom"></span>
              <span class="setting-text">
                <strong>公开</strong>
                <small>所有人可见，可能出现在推荐流</small>
              </span>
            </label>
          </div>
          <div class="setting-item">
            <label class="setting-label">
              <input
                type="radio"
                v-model="videoForm.privacy"
                value="private"
                name="privacy"
              />
              <span class="radio-custom"></span>
              <span class="setting-text">
                <strong>私密</strong>
                <small>仅自己可见，他人无法观看</small>
              </span>
            </label>
          </div>
        </div>

        </section>
    </main>

    <!-- 发布进度弹窗 -->
    <div v-if="showPublishProgress" class="publish-modal">
      <div class="publish-content">
        <div class="publish-icon">
          <svg viewBox="0 0 24 24" fill="currentColor">
            <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
          </svg>
        </div>
        <h3>正在发布视频</h3>
        <div class="progress-bar">
          <div class="progress-fill" :style="{ width: publishProgress + '%' }"></div>
        </div>
        <p>{{ publishProgress }}%</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'

const router = useRouter()
const authStore = useAuthStore()

// 响应式数据
const videoInput = ref<HTMLInputElement>()
const coverInput = ref<HTMLInputElement>()
const videoFile = ref<File>()
const coverFile = ref<File>()
const videoPreview = ref('')
const coverPreview = ref('')
const isUploading = ref(false)
const uploadProgress = ref(0)
const isDragover = ref(false)
const tagInput = ref('')
const showPublishProgress = ref(false)
const publishProgress = ref(0)

// 视频表单数据
const videoForm = ref({
  description: '',
  tags: [] as string[],
  privacy: 'public' as 'public' | 'private'
})

// 推荐标签
const suggestedTags = ['搞笑', '音乐', '舞蹈', '美食', '旅行', '生活', '萌宠', '游戏', '运动', '时尚']

// 计算属性
const canPublish = computed(() => {
  return videoFile.value && videoForm.value.description.trim().length > 0
})

// 方法
const triggerVideoUpload = () => {
  videoInput.value?.click()
}

const triggerCoverUpload = () => {
  coverInput.value?.click()
}

const handleVideoSelect = (event: Event) => {
  const file = (event.target as HTMLInputElement).files?.[0]
  if (file) {
    processVideoFile(file)
  }
}

const handleCoverSelect = (event: Event) => {
  const file = (event.target as HTMLInputElement).files?.[0]
  if (file) {
    processCoverFile(file)
  }
}

const handleDrop = (event: DragEvent) => {
  event.preventDefault()
  isDragover.value = false

  const files = event.dataTransfer?.files
  if (files && files.length > 0) {
    const file = files[0]
    if (file && file.type.startsWith('video/')) {
      processVideoFile(file)
    } else {
      ElMessage.error('请上传视频文件')
    }
  }
}

const processVideoFile = (file: File) => {
  if (file.size > 2 * 1024 * 1024 * 1024) {
    ElMessage.error('视频文件大小不能超过 2GB')
    return
  }

  videoFile.value = file
  const url = URL.createObjectURL(file)
  videoPreview.value = url
  ElMessage.success('视频已选择')
}

const processCoverFile = (file: File) => {
  if (!file.type.startsWith('image/')) {
    ElMessage.error('请选择图片文件作为封面')
    return
  }

  coverFile.value = file
  const url = URL.createObjectURL(file)
  coverPreview.value = url
  ElMessage.success('封面已选择')
}

const removeVideo = () => {
  videoFile.value = undefined
  videoPreview.value = ''
  if (videoInput.value) {
    videoInput.value.value = ''
  }
}

const removeCover = () => {
  coverFile.value = undefined
  coverPreview.value = ''
  if (coverInput.value) {
    coverInput.value.value = ''
  }
}

const autoGenerateCover = async () => {
  if (!videoFile.value) {
    ElMessage.warning('请先上传视频')
    return
  }

  ElMessage.info('正在生成封面...')
  // 这里可以调用视频截帧的API
  // 暂时使用默认图片
  setTimeout(() => {
    coverPreview.value = 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTA4MCIgaGVpZ2h0PSIxOTIwIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciPjxyZWN0IHdpZHRoPSIxMDgwIiBoZWlnaHQ9IjE5MjAiIGZpbGw9IiNmMGYwZjAiLz48dGV4dCB4PSI1NDAiIHk9Ijk2MCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjI0IiBmaWxsPSIjOTk5IiB0ZXh0LWFuY2hvcj0ibWlkZGxlIj7kuK3vvJrmnKvlvankuY3kvankurrlpJbnmoTnoLTorrA8L3RleHQ+PC9zdmc+'
    ElMessage.success('封面已自动生成')
  }, 1000)
}

const addTag = () => {
  const tag = tagInput.value.trim()
  if (tag && !videoForm.value.tags.includes(tag)) {
    if (videoForm.value.tags.length >= 5) {
      ElMessage.warning('最多添加5个标签')
      return
    }
    videoForm.value.tags.push(tag)
    tagInput.value = ''
  }
}

const removeTag = (index: number) => {
  videoForm.value.tags.splice(index, 1)
}

const addSuggestedTag = (tag: string) => {
  if (!videoForm.value.tags.includes(tag)) {
    if (videoForm.value.tags.length >= 5) {
      ElMessage.warning('最多添加5个标签')
      return
    }
    videoForm.value.tags.push(tag)
  }
}

const saveDraft = () => {
  if (!videoFile.value) {
    ElMessage.warning('请先上传视频')
    return
  }

  // 这里调用保存草稿的API
  ElMessage.success('草稿已保存')
}

const publishVideo = async () => {
  if (!canPublish.value) {
    ElMessage.warning('请完善视频信息')
    return
  }

  try {
    showPublishProgress.value = true
    publishProgress.value = 0

    // 模拟发布进度
    const progressInterval = setInterval(() => {
      publishProgress.value += Math.random() * 20
      if (publishProgress.value >= 100) {
        publishProgress.value = 100
        clearInterval(progressInterval)

        setTimeout(() => {
          showPublishProgress.value = false
          ElMessage.success('视频发布成功！')
          router.push('/profile')
        }, 500)
      }
    }, 800)

    // 这里调用真实的发布API
    console.log('发布视频数据:', {
      video: videoFile.value,
      cover: coverFile.value,
      ...videoForm.value
    })

  } catch (error) {
    console.error('发布失败:', error)
    ElMessage.error('发布失败，请重试')
    showPublishProgress.value = false
  }
}

// 组件挂载时获取用户信息
onMounted(async () => {
  // 如果有token但没有用户信息，获取用户信息
  if (!authStore.user) {
    try {
      await authStore.fetchUserProfile()
    } catch (error) {
      ElMessage.error('获取用户信息失败，请重新登录')
      authStore.logout()
      router.push('/')
      return
    }
  }
})
</script>

<style scoped>
.create-page {
  width: 100%;
  background: transparent;
  min-height: calc(100vh - 64px);
  padding: 20px 40px 20px 20px;
}

/* 创作页面头部 */
.create-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #2b2d38;
}

.create-title {
  font-size: 28px;
  font-weight: 700;
  color: #fff;
  margin: 0;
}

.header-actions {
  display: flex;
  gap: 16px;
}

.draft-btn, .publish-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 24px;
  border-radius: 8px;
  font-weight: 600;
  font-size: 16px;
  transition: all 0.3s ease;
  border: none;
  cursor: pointer;
}

.draft-btn {
  background: #2b2d38;
  color: #888;
}

.draft-btn:hover:not(:disabled) {
  background: #3a3d48;
  color: #aaa;
}

.publish-btn {
  background: linear-gradient(135deg, #fe2c55 0%, #ff6b6b 100%);
  color: white;
}

.publish-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, #e02549 0%, #ff5252 100%);
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(254, 44, 85, 0.3);
}

.publish-btn:disabled, .draft-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.draft-btn svg, .publish-btn svg {
  width: 18px;
  height: 18px;
}

/* 创作内容区域 */
.create-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 30px;
  height: fit-content;
  min-height: 600px;
}

/* 上传区域 */
.upload-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
  height: 100%;
}

.upload-card {
  background: #161823;
  border-radius: 16px;
  padding: 25px;
  border: 1px solid #2b2d38;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.card-title {
  font-size: 18px;
  font-weight: 600;
  color: #fff;
  margin: 0 0 20px 0;
}

/* 视频上传区 */
.video-upload-zone {
  width: 100%;
  height: 300px;
  border: 2px dashed #3a3d48;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  background: #1a1d29;
  flex: 1;
}

.video-upload-zone:hover {
  border-color: #fe2c55;
  background: rgba(254, 44, 85, 0.05);
}

.video-upload-zone.dragover {
  border-color: #fe2c55;
  background: rgba(254, 44, 85, 0.1);
}

.video-upload-zone.has-video {
  border: none;
  background: #0a0a0a;
}

/* 封面上传区 */
.cover-upload-zone {
  width: 100%;
  height: 200px;
  border: 2px dashed #3a3d48;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  background: #1a1d29;
  flex: 1;
}

.cover-upload-zone:hover {
  border-color: #fe2c55;
  background: rgba(254, 44, 85, 0.05);
}

.cover-upload-zone.has-cover {
  border: none;
  background: #0a0a0a;
}

/* 上传占位符 */
.upload-placeholder {
  text-align: center;
  color: #666;
}

.upload-icon {
  width: 48px;
  height: 48px;
  margin-bottom: 16px;
  opacity: 0.6;
}

.upload-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 8px;
}

.upload-hint {
  font-size: 14px;
  color: #555;
}

/* 上传中状态 */
.uploading-state {
  text-align: center;
  color: #fff;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid #333;
  border-top: 3px solid #fe2c55;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 16px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.upload-progress {
  font-size: 18px;
  font-weight: 600;
  color: #fe2c55;
  margin-bottom: 8px;
}

.upload-text {
  font-size: 14px;
  color: #888;
}

/* 视频预览 */
.video-preview {
  width: 100%;
  height: 100%;
  position: relative;
}

.preview-video {
  width: 100%;
  height: 100%;
  object-fit: contain;
  border-radius: 8px;
}

.remove-video-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  width: 32px;
  height: 32px;
  background: rgba(0, 0, 0, 0.7);
  border: none;
  border-radius: 50%;
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.remove-video-btn:hover {
  background: rgba(254, 44, 85, 0.8);
}

/* 封面预览 */
.cover-preview {
  width: 100%;
  height: 100%;
  position: relative;
}

.preview-cover {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

.remove-cover-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  width: 32px;
  height: 32px;
  background: rgba(0, 0, 0, 0.7);
  border: none;
  border-radius: 50%;
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.remove-cover-btn:hover {
  background: rgba(254, 44, 85, 0.8);
}

.auto-generate-btn {
  position: absolute;
  bottom: 10px;
  right: 10px;
  padding: 8px 16px;
  background: rgba(254, 44, 85, 0.9);
  border: none;
  border-radius: 6px;
  color: white;
  font-size: 12px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: all 0.3s ease;
}

.auto-generate-btn:hover:not(:disabled) {
  background: #fe2c55;
}

.auto-generate-btn:disabled {
  background: #555;
  cursor: not-allowed;
}

.auto-generate-btn svg {
  width: 14px;
  height: 14px;
}

/* 信息填写区 */
.info-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
  height: 100%;
}

.info-card {
  background: #161823;
  border-radius: 16px;
  padding: 25px;
  border: 1px solid #2b2d38;
  flex: 1;
  display: flex;
  flex-direction: column;
}

/* 文案输入 */
.description-input {
  width: 100%;
  background: #1a1d29;
  border: 1px solid #3a3d48;
  border-radius: 8px;
  padding: 16px;
  color: #fff;
  font-size: 16px;
  line-height: 1.5;
  resize: vertical;
  min-height: 120px;
  font-family: inherit;
  transition: all 0.3s ease;
}

.description-input:focus {
  outline: none;
  border-color: #fe2c55;
  background: #22252f;
}

.description-input::placeholder {
  color: #666;
}

.char-count {
  text-align: right;
  font-size: 12px;
  color: #666;
  margin-top: 8px;
}

/* 标签输入 */
.tag-input-wrapper {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.tag-input {
  flex: 1;
  background: #1a1d29;
  border: 1px solid #3a3d48;
  border-radius: 8px;
  padding: 12px 16px;
  color: #fff;
  font-size: 16px;
  transition: all 0.3s ease;
}

.tag-input:focus {
  outline: none;
  border-color: #fe2c55;
  background: #22252f;
}

.tag-input::placeholder {
  color: #666;
}

.add-tag-btn {
  padding: 12px 20px;
  background: #2b2d38;
  border: 1px solid #3a3d48;
  border-radius: 8px;
  color: #fff;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.add-tag-btn:hover {
  background: #3a3d48;
  border-color: #fe2c55;
}

/* 标签列表 */
.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 16px;
  min-height: 40px;
}

.tag-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: rgba(254, 44, 85, 0.1);
  border: 1px solid rgba(254, 44, 85, 0.3);
  border-radius: 20px;
  color: #fe2c55;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.remove-tag-btn {
  width: 20px;
  height: 20px;
  background: rgba(254, 44, 85, 0.9);
  border: 1px solid rgba(254, 44, 85, 0.95);
  border-radius: 50%;
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  font-size: 16px;
  font-weight: 600;
  font-family: Arial, sans-serif;
  line-height: 1;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.remove-tag-btn:hover {
  background: #fe2c55;
  border-color: #fe2c55;
  transform: scale(1.15);
  box-shadow: 0 3px 12px rgba(254, 44, 85, 0.3);
}

/* 推荐标签 */
.suggested-tags {
  padding-top: 16px;
  border-top: 1px solid #2b2d38;
}

.suggested-title {
  font-size: 14px;
  color: #888;
  margin-bottom: 12px;
  display: block;
}

.suggested-tag {
  padding: 6px 12px;
  background: #2b2d38;
  border: 1px solid #3a3d48;
  border-radius: 16px;
  color: #aaa;
  font-size: 12px;
  cursor: pointer;
  margin-right: 8px;
  margin-bottom: 8px;
  transition: all 0.3s ease;
  display: inline-block;
}

.suggested-tag:hover {
  background: rgba(254, 44, 85, 0.1);
  border-color: #fe2c55;
  color: #fe2c55;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(254, 44, 85, 0.2);
}

/* 发布设置 */
.setting-item {
  margin-bottom: 20px;
}

.setting-label {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  cursor: pointer;
  color: #fff;
}

.setting-text {
  flex: 1;
}

.setting-text strong {
  display: block;
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 4px;
}

.setting-text small {
  display: block;
  font-size: 14px;
  color: #888;
  line-height: 1.4;
}

/* 单选框样式 */
input[type="radio"] {
  display: none;
}

.radio-custom {
  width: 20px;
  height: 20px;
  border: 2px solid #3a3d48;
  border-radius: 50%;
  position: relative;
  transition: all 0.3s ease;
  flex-shrink: 0;
  margin-top: 2px;
}

input[type="radio"]:checked + .radio-custom {
  border-color: #fe2c55;
  background: #fe2c55;
}

input[type="radio"]:checked + .radio-custom::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 8px;
  height: 8px;
  background: white;
  border-radius: 50%;
}

/* 复选框样式 */
input[type="checkbox"] {
  display: none;
}

.checkbox-custom {
  width: 20px;
  height: 20px;
  border: 2px solid #3a3d48;
  border-radius: 4px;
  position: relative;
  transition: all 0.3s ease;
  flex-shrink: 0;
  margin-top: 2px;
}

input[type="checkbox"]:checked + .checkbox-custom {
  border-color: #fe2c55;
  background: #fe2c55;
}

input[type="checkbox"]:checked + .checkbox-custom::after {
  content: '✓';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: white;
  font-size: 14px;
  font-weight: bold;
}


/* 发布进度弹窗 */
.publish-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.publish-content {
  background: #161823;
  border-radius: 16px;
  padding: 40px;
  text-align: center;
  border: 1px solid #2b2d38;
  min-width: 300px;
}

.publish-icon {
  width: 48px;
  height: 48px;
  color: #fe2c55;
  margin: 0 auto 20px;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; transform: scale(1); }
  50% { opacity: 0.8; transform: scale(1.1); }
}

.publish-content h3 {
  color: #fff;
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 24px;
}

.progress-bar {
  width: 100%;
  height: 8px;
  background: #2b2d38;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 16px;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #fe2c55, #ff6b6b);
  border-radius: 4px;
  transition: width 0.3s ease;
}

.publish-content p {
  color: #fe2c55;
  font-size: 18px;
  font-weight: 600;
  margin: 0;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .create-container {
    grid-template-columns: 1fr;
  }

  .upload-section {
    order: 1;
  }

  .info-section {
    order: 2;
  }
}

@media (max-width: 768px) {
  .create-page {
    padding: 20px;
  }

  .create-header {
    flex-direction: column;
    gap: 20px;
    align-items: stretch;
  }

  .header-actions {
    justify-content: center;
  }

  .upload-card, .info-card {
    padding: 20px;
  }

  .video-upload-zone {
    height: 250px;
  }

  .cover-upload-zone {
    height: 150px;
  }
}
</style>