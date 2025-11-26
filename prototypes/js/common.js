// 公共组件加载和初始化脚本

// 登录状态管理
let isUserLoggedIn = false;

// 顶部导航栏HTML（动态生成）
function getTopbarHTML() {
    return `
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
    ${isUserLoggedIn ? `
        <div class="user-avatar" onclick="showUserMenu()" title="个人中心">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="white">
                <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
            </svg>
        </div>
    ` : `
        <button class="login-btn" onclick="showAuthModal('login')" title="登录">登录</button>
    `}
</div>
`;
}

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

<a href="profile.html" class="nav-item" data-page="profile">
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
        topbarContainer.innerHTML = getTopbarHTML();
    }
    
    // 加载左侧导航栏
    const sidebarContainer = document.getElementById('sidebar-container');
    if (sidebarContainer) {
        sidebarContainer.innerHTML = sidebarHTML;
    }
    
    // 加载登录注册弹窗
    loadAuthModal();
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
    // 检查登录状态
    checkLoginStatus();
    
    // 加载组件
    loadComponents();
    
    // 设置当前页面的激活状态
    const currentPage = getCurrentPageName();
    setActiveNavItem(currentPage);
}

// 检查登录状态（从 localStorage 读取）
function checkLoginStatus() {
    const token = localStorage.getItem('accessToken');
    isUserLoggedIn = !!token;
}

// 设置登录状态
function setLoginStatus(loggedIn, token = null) {
    isUserLoggedIn = loggedIn;
    if (loggedIn && token) {
        localStorage.setItem('accessToken', token);
    } else {
        localStorage.removeItem('accessToken');
    }
    // 重新加载顶部导航栏
    loadComponents();
}

// 显示认证弹窗
function showAuthModal(mode = 'login') {
    const loginModal = document.getElementById('authLoginModal');
    const registerModal = document.getElementById('authRegisterModal');
    
    if (mode === 'login' && loginModal) {
        loginModal.classList.add('active');
    } else if (mode === 'register' && registerModal) {
        registerModal.classList.add('active');
    }
}

// 关闭认证弹窗
function closeAuthModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.remove('active');
    }
}

// 切换认证模式
function switchAuthMode(fromMode, toMode) {
    closeAuthModal(fromMode === 'login' ? 'authLoginModal' : 'authRegisterModal');
    setTimeout(() => showAuthModal(toMode), 300);
}

// 显示用户菜单
function showUserMenu() {
    alert('个人中心菜单\n\n- 我的主页\n- 账号设置\n- 退出登录');
}

// 加载登录注册弹窗
function loadAuthModal() {
    // 检查是否已加载
    if (document.getElementById('authLoginModal')) {
        return;
    }
    
    // 创建弹窗容器
    const modalContainer = document.createElement('div');
    modalContainer.innerHTML = getAuthModalHTML();
    document.body.appendChild(modalContainer);
    
    // 添加样式
    const style = document.createElement('style');
    style.textContent = getAuthModalStyles();
    document.head.appendChild(style);
}

// 获取认证弹窗HTML
function getAuthModalHTML() {
    return `
    <!-- 登录弹窗 -->
    <div class="auth-modal-overlay" id="authLoginModal">
        <div class="auth-modal-container">
            <button class="auth-modal-close" onclick="closeAuthModal('authLoginModal')">&times;</button>
            
            <div class="auth-modal-header">
                <h2 class="auth-modal-title">欢迎回来</h2>
                <p class="auth-modal-subtitle">登录 FunFlow 继续精彩内容</p>
            </div>

            <div class="auth-modal-body">
                <form onsubmit="handleLogin(event)">
                    <div class="auth-form-group">
                        <label class="auth-form-label">邮箱</label>
                        <input type="email" class="auth-form-input" placeholder="请输入邮箱地址" required>
                    </div>

                    <div class="auth-form-group">
                        <label class="auth-form-label">密码</label>
                        <input type="password" class="auth-form-input" placeholder="请输入密码" required>
                    </div>

                    <div class="auth-form-group">
                        <label class="auth-form-label">图形验证码</label>
                        <div class="auth-captcha-row">
                            <div class="auth-captcha-input">
                                <input type="text" class="auth-form-input" placeholder="请输入验证码" required>
                            </div>
                            <div class="auth-captcha-image" onclick="refreshAuthCaptcha(this)">A3b7</div>
                        </div>
                    </div>

                    <button type="submit" class="auth-primary-btn">登录</button>

                    <div class="auth-switch-mode">
                        还没有账号？<a class="auth-switch-link" onclick="switchAuthMode('login', 'register')">立即注册</a>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- 注册弹窗 -->
    <div class="auth-modal-overlay" id="authRegisterModal">
        <div class="auth-modal-container">
            <button class="auth-modal-close" onclick="closeAuthModal('authRegisterModal')">&times;</button>
            
            <div class="auth-modal-header">
                <h2 class="auth-modal-title">加入 FunFlow</h2>
                <p class="auth-modal-subtitle">创建账号，开启创作之旅</p>
            </div>

            <div class="auth-modal-body">
                <form onsubmit="handleRegister(event)">
                    <div class="auth-form-group">
                        <label class="auth-form-label">邮箱</label>
                        <input type="email" class="auth-form-input" id="authRegisterEmail" placeholder="请输入邮箱地址" required>
                    </div>

                    <div class="auth-form-group">
                        <label class="auth-form-label">图形验证码</label>
                        <div class="auth-captcha-row">
                            <div class="auth-captcha-input">
                                <input type="text" class="auth-form-input" id="authRegisterCaptcha" placeholder="请输入验证码" required>
                            </div>
                            <div class="auth-captcha-image" onclick="refreshAuthCaptcha(this)">X8y9</div>
                        </div>
                        <div class="auth-info-message">
                            🔒 图形验证码用于防止恶意注册
                        </div>
                    </div>

                    <div class="auth-form-group">
                        <label class="auth-form-label">邮箱验证码</label>
                        <div class="auth-email-code-row">
                            <div class="auth-email-code-input">
                                <input type="text" class="auth-form-input" placeholder="请输入6位验证码" maxlength="6" required>
                            </div>
                            <button type="button" class="auth-send-code-btn" id="authSendCodeBtn" onclick="sendAuthEmailCode()">
                                发送验证码
                            </button>
                        </div>
                        <div class="auth-info-message">
                            📧 验证码将发送到您的邮箱，5分钟内有效
                        </div>
                    </div>

                    <div class="auth-form-group">
                        <label class="auth-form-label">设置密码</label>
                        <input type="password" class="auth-form-input" id="authRegisterPassword" 
                               placeholder="8-32位，包含字母和数字" 
                               oninput="checkAuthPasswordStrength(this.value)" required>
                        <div class="auth-password-strength">
                            <div class="auth-password-strength-bar" id="authStrengthBar"></div>
                        </div>
                        <div class="auth-password-strength-text" id="authStrengthText"></div>
                    </div>

                    <button type="submit" class="auth-primary-btn">注册</button>

                    <div class="auth-switch-mode">
                        已有账号？<a class="auth-switch-link" onclick="switchAuthMode('register', 'login')">立即登录</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
    `;
}

// 获取认证弹窗样式
function getAuthModalStyles() {
    return `
        /* 登录按钮样式 */
        .login-btn {
            padding: 8px 24px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 20px;
            font-size: 14px;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.3s;
        }

        .login-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
        }

        /* 认证弹窗遮罩层 */
        .auth-modal-overlay {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.75);
            backdrop-filter: blur(8px);
            z-index: 99999;
            animation: authFadeIn 0.3s ease;
        }

        .auth-modal-overlay.active {
            display: flex;
            justify-content: center;
            align-items: center;
        }

        @keyframes authFadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }

        /* 认证弹窗容器 */
        .auth-modal-container {
            background: #161823;
            border-radius: 16px;
            width: 480px;
            max-width: 90%;
            max-height: 90vh;
            overflow-y: auto;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.5);
            animation: authSlideUp 0.3s ease;
            position: relative;
        }

        @keyframes authSlideUp {
            from {
                transform: translateY(50px);
                opacity: 0;
            }
            to {
                transform: translateY(0);
                opacity: 1;
            }
        }

        /* 关闭按钮 */
        .auth-modal-close {
            position: absolute;
            top: 20px;
            right: 20px;
            width: 32px;
            height: 32px;
            border-radius: 50%;
            background: rgba(255, 255, 255, 0.1);
            border: none;
            color: #8a8b8e;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 20px;
            transition: all 0.3s;
            z-index: 10;
        }

        .auth-modal-close:hover {
            background: rgba(255, 255, 255, 0.2);
            color: #fff;
            transform: rotate(90deg);
        }

        /* 弹窗头部 */
        .auth-modal-header {
            padding: 40px 40px 20px;
            text-align: center;
        }

        .auth-modal-title {
            font-size: 28px;
            font-weight: 700;
            margin-bottom: 8px;
            background: linear-gradient(90deg, #4A9EFF 0%, #A78BFA 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .auth-modal-subtitle {
            font-size: 14px;
            color: #8a8b8e;
        }

        /* 弹窗内容 */
        .auth-modal-body {
            padding: 20px 40px 40px;
        }

        /* 表单组 */
        .auth-form-group {
            margin-bottom: 24px;
        }

        .auth-form-label {
            display: block;
            font-size: 14px;
            color: #e4e6eb;
            margin-bottom: 8px;
            font-weight: 500;
        }

        .auth-form-input {
            width: 100%;
            padding: 14px 16px;
            background: #2b2d38;
            border: 1px solid #3a3a3a;
            border-radius: 10px;
            color: #fff;
            font-size: 14px;
            outline: none;
            transition: all 0.3s;
        }

        .auth-form-input:focus {
            border-color: #667eea;
            background: #1e1f29;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        .auth-form-input::placeholder {
            color: #6b6c6f;
        }

        /* 验证码输入框 */
        .auth-captcha-row {
            display: flex;
            gap: 12px;
            align-items: flex-end;
        }

        .auth-captcha-input {
            flex: 1;
        }

        .auth-captcha-image {
            width: 120px;
            height: 48px;
            background: #2b2d38;
            border: 1px solid #3a3a3a;
            border-radius: 10px;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 20px;
            font-weight: 700;
            letter-spacing: 4px;
            color: #667eea;
            position: relative;
            overflow: hidden;
            user-select: none;
            transition: all 0.3s;
        }

        .auth-captcha-image:hover {
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        .auth-captcha-image::after {
            content: '点击刷新';
            position: absolute;
            bottom: 2px;
            right: 4px;
            font-size: 9px;
            color: #6b6c6f;
            letter-spacing: 0;
        }

        /* 邮箱验证码输入框 */
        .auth-email-code-row {
            display: flex;
            gap: 12px;
        }

        .auth-email-code-input {
            flex: 1;
        }

        .auth-send-code-btn {
            padding: 14px 20px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 14px;
            font-weight: 500;
            cursor: pointer;
            white-space: nowrap;
            transition: all 0.3s;
        }

        .auth-send-code-btn:hover:not(:disabled) {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
        }

        .auth-send-code-btn:disabled {
            background: #2b2d38;
            color: #6b6c6f;
            cursor: not-allowed;
        }

        /* 主按钮 */
        .auth-primary-btn {
            width: 100%;
            padding: 16px;
            background: linear-gradient(135deg, #fe2c55 0%, #ff6b6b 100%);
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
            margin-top: 8px;
        }

        .auth-primary-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(254, 44, 85, 0.4);
        }

        /* 切换提示 */
        .auth-switch-mode {
            text-align: center;
            margin-top: 24px;
            padding-top: 24px;
            border-top: 1px solid #2b2d38;
            font-size: 14px;
            color: #8a8b8e;
        }

        .auth-switch-link {
            color: #667eea;
            text-decoration: none;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.3s;
        }

        .auth-switch-link:hover {
            color: #8b7fff;
            text-decoration: underline;
        }

        /* 提示信息 */
        .auth-info-message {
            margin-top: 12px;
            padding: 12px;
            background: rgba(102, 126, 234, 0.1);
            border: 1px solid rgba(102, 126, 234, 0.3);
            border-radius: 8px;
            font-size: 13px;
            color: #8a8b8e;
            line-height: 1.6;
        }

        /* 密码强度指示器 */
        .auth-password-strength {
            margin-top: 8px;
            height: 4px;
            background: #2b2d38;
            border-radius: 2px;
            overflow: hidden;
        }

        .auth-password-strength-bar {
            height: 100%;
            width: 0%;
            transition: all 0.3s;
            border-radius: 2px;
        }

        .auth-password-strength-bar.weak {
            width: 33%;
            background: #ff6b6b;
        }

        .auth-password-strength-bar.medium {
            width: 66%;
            background: #ffd93d;
        }

        .auth-password-strength-bar.strong {
            width: 100%;
            background: #20d5a1;
        }

        .auth-password-strength-text {
            margin-top: 4px;
            font-size: 12px;
            color: #8a8b8e;
        }

        /* 滚动条样式 */
        .auth-modal-container::-webkit-scrollbar {
            width: 8px;
        }

        .auth-modal-container::-webkit-scrollbar-track {
            background: #161823;
        }

        .auth-modal-container::-webkit-scrollbar-thumb {
            background: #2b2d38;
            border-radius: 4px;
        }

        .auth-modal-container::-webkit-scrollbar-thumb:hover {
            background: #3a3a3a;
        }
    `;
}

// 刷新图形验证码
function refreshAuthCaptcha(element) {
    const chars = 'ABCDEFGHJKLMNPQRSTUVWXYZ23456789';
    let code = '';
    for (let i = 0; i < 4; i++) {
        code += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    element.textContent = code;
    
    // 添加刷新动画
    element.style.transform = 'scale(0.9)';
    setTimeout(() => {
        element.style.transform = 'scale(1)';
    }, 100);
}

// 发送邮箱验证码
let authCountdown = 0;
function sendAuthEmailCode() {
    const email = document.getElementById('authRegisterEmail').value;
    const captcha = document.getElementById('authRegisterCaptcha').value;
    const btn = document.getElementById('authSendCodeBtn');
    
    if (!email) {
        alert('请先输入邮箱地址');
        return;
    }
    
    if (!captcha) {
        alert('请先输入图形验证码');
        return;
    }
    
    // TODO: 实际调用接口 POST /api/auth/send-email-code
    console.log('发送邮箱验证码:', { email, captcha });
    alert('✅ 验证码已发送到您的邮箱！');
    
    // 开始倒计时
    authCountdown = 60;
    btn.disabled = true;
    
    const timer = setInterval(() => {
        authCountdown--;
        btn.textContent = `${authCountdown}秒后重试`;
        
        if (authCountdown <= 0) {
            clearInterval(timer);
            btn.disabled = false;
            btn.textContent = '发送验证码';
        }
    }, 1000);
}

// 检查密码强度
function checkAuthPasswordStrength(password) {
    const bar = document.getElementById('authStrengthBar');
    const text = document.getElementById('authStrengthText');
    
    if (!bar || !text) return;
    
    if (password.length === 0) {
        bar.className = 'auth-password-strength-bar';
        text.textContent = '';
        return;
    }
    
    let strength = 0;
    if (password.length >= 8) strength++;
    if (password.match(/[a-z]/)) strength++;
    if (password.match(/[A-Z]/)) strength++;
    if (password.match(/[0-9]/)) strength++;
    if (password.match(/[^a-zA-Z0-9]/)) strength++;
    
    bar.className = 'auth-password-strength-bar';
    
    if (strength <= 2) {
        bar.classList.add('weak');
        text.textContent = '密码强度：弱';
        text.style.color = '#ff6b6b';
    } else if (strength <= 3) {
        bar.classList.add('medium');
        text.textContent = '密码强度：中等';
        text.style.color = '#ffd93d';
    } else {
        bar.classList.add('strong');
        text.textContent = '密码强度：强';
        text.style.color = '#20d5a1';
    }
}

// 处理登录
function handleLogin(event) {
    event.preventDefault();
    const form = event.target;
    const email = form.querySelector('input[type="email"]').value;
    const password = form.querySelector('input[type="password"]').value;
    
    // TODO: 实际调用接口 POST /api/auth/login
    console.log('登录:', { email, password });
    
    // 模拟登录成功
    const mockToken = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...';
    setLoginStatus(true, mockToken);
    closeAuthModal('authLoginModal');
    alert('✅ 登录成功！');
}

// 处理注册
function handleRegister(event) {
    event.preventDefault();
    const form = event.target;
    const email = form.querySelector('#authRegisterEmail').value;
    const password = form.querySelector('#authRegisterPassword').value;
    
    // TODO: 实际调用接口 POST /api/auth/register
    console.log('注册:', { email, password });
    
    alert('✅ 注册成功！请登录');
    closeAuthModal('authRegisterModal');
    setTimeout(() => showAuthModal('login'), 500);
}

// 点击遮罩层关闭弹窗
document.addEventListener('click', (e) => {
    if (e.target.classList.contains('auth-modal-overlay')) {
        closeAuthModal(e.target.id);
    }
});

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', initCommonComponents);

