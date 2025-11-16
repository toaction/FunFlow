// 公共组件加载和初始化脚本

// 顶部导航栏HTML
const topbarHTML = `
<div class="logo-section">
    <div class="logo-icon">
        <img src="../Pictures/logo/logo.jpeg" alt="FunFlow Logo">
    </div>
    <span>FunFlow</span>
</div>

<div class="search-section">
    <div class="search-input-wrapper">
        <input type="text" class="search-input" placeholder="输入搜索关键词">
        <svg class="search-icon" width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
            <path d="M15.5 14h-.79l-.28-.27A6.471 6.471 0 0016 9.5 6.5 6.5 0 109.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z"/>
        </svg>
    </div>
</div>

<div class="user-section">
    <div class="user-avatar">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="white">
            <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
        </svg>
    </div>
</div>
`;

// 左侧导航栏HTML
const sidebarHTML = `
<a href="hot.html" class="nav-item" data-page="hot">
    <svg class="nav-icon" viewBox="0 0 24 24" fill="currentColor">
        <path d="M16 6l2.29 2.29-4.88 4.88-4-4L2 16.59 3.41 18l6-6 4 4 6.3-6.29L22 12V6z"/>
    </svg>
    <span>热门视频</span>
</a>

<a href="index.html" class="nav-item" data-page="index">
    <svg class="nav-icon" viewBox="0 0 24 24" fill="currentColor">
        <path d="M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z"/>
    </svg>
    <span>首页推荐</span>
</a>

<a href="follow.html" class="nav-item" data-page="follow">
    <svg class="nav-icon" viewBox="0 0 24 24" fill="currentColor">
        <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
    </svg>
    <span>我的关注</span>
</a>

<a href="home.html" class="nav-item" data-page="home">
    <svg class="nav-icon" viewBox="0 0 24 24" fill="currentColor">
        <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 3c1.66 0 3 1.34 3 3s-1.34 3-3 3-3-1.34-3-3 1.34-3 3-3zm0 14.2c-2.5 0-4.71-1.28-6-3.22.03-1.99 4-3.08 6-3.08 1.99 0 5.97 1.09 6 3.08-1.29 1.94-3.5 3.22-6 3.22z"/>
    </svg>
    <span>个人中心</span>
</a>
`;

// 加载组件
function loadComponents() {
    // 加载顶部导航栏
    const topbarContainer = document.getElementById('topbar-container');
    if (topbarContainer) {
        topbarContainer.innerHTML = topbarHTML;
    }
    
    // 加载左侧导航栏
    const sidebarContainer = document.getElementById('sidebar-container');
    if (sidebarContainer) {
        sidebarContainer.innerHTML = sidebarHTML;
    }
}

// 设置导航栏激活状态
function setActiveNavItem(pageName) {
    const navItems = document.querySelectorAll('.nav-item');
    navItems.forEach(item => {
        item.classList.remove('active');
        if (item.getAttribute('data-page') === pageName) {
            item.classList.add('active');
        }
    });
}

// 获取当前页面名称
function getCurrentPageName() {
    const path = window.location.pathname;
    const filename = path.split('/').pop();
    return filename.replace('.html', '');
}

// 初始化公共组件
function initCommonComponents() {
    // 加载组件
    loadComponents();
    
    // 设置当前页面的激活状态
    const currentPage = getCurrentPageName();
    setActiveNavItem(currentPage);
}

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', initCommonComponents);

