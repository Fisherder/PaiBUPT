import {
    createSSRApp
} from "vue";
import uView from './uni_modules/vk-uview-ui';
import App from "./App.vue";

// 创建简单的事件总线
const eventBus = {
    events: {},
    emit(event, ...args) {
        if (this.events[event]) {
            this.events[event].forEach(callback => {
                callback(...args);
            });
        }
    },
    on(event, callback) {
        if (!this.events[event]) {
            this.events[event] = [];
        }
        this.events[event].push(callback);
    },
    off(event, callback) {
        if (this.events[event]) {
            if (callback) {
                this.events[event] = this.events[event].filter(cb => cb !== callback);
            } else {
                this.events[event] = [];
            }
        }
    }
};

export function createApp() {
    const app = createSSRApp(App);
    
    //使用uview ui
    app.use(uView);
    
    // 添加全局事件总线
    app.config.globalProperties.$eventBus = eventBus;
    
    // 添加全局应用数据
    app.config.globalProperties.$appData = {
        isLoggedIn: false,
        userInfo: null,
        
        // 检查是否已登录
        checkLogin: function() {
            return !!uni.getStorageSync('userId');
        },
        
        // 获取用户信息
        getUserInfo: function() {
            return uni.getStorageSync('userInfo') || null;
        },
        
        // 设置用户信息
        setUserInfo: function(info) {
            if (info) {
                uni.setStorageSync('userInfo', info);
                this.userInfo = info;
                // 发出用户信息更新事件
                eventBus.emit('userInfo-updated', info);
            }
        },
        
        // 登录成功处理
        loginSuccess: function(userInfo) {
            // 存储用户信息
            if (userInfo && userInfo.userId) {
                uni.setStorageSync('userId', userInfo.userId);
                this.setUserInfo(userInfo);
                this.isLoggedIn = true;
                // 触发登录成功事件
                eventBus.emit('login-success', userInfo);
            }
        },
        
        // 退出登录处理
        logout: function() {
            uni.removeStorageSync('userId');
            uni.removeStorageSync('userInfo');
            this.userInfo = null;
            this.isLoggedIn = false;
            // 触发退出登录事件
            eventBus.emit('logout');
        },
        
        // 需要登录检查
        requireLogin: function(callback) {
            if (this.checkLogin()) {
                if (callback) callback();
                return true;
            } else {
                uni.showModal({
                    title: '提示',
                    content: '该功能需要登录后才能使用',
                    confirmText: '去登录',
                    cancelText: '取消',
                    success: function(res) {
                        if (res.confirm) {
                            uni.navigateTo({
                                url: "/pages/login/login"
                            });
                        }
                    }
                });
                return false;
            }
        }
    };
    
    // 初始化登录状态
    const userId = uni.getStorageSync('userId');
    app.config.globalProperties.$appData.isLoggedIn = !!userId;
    
    if (userId) {
        app.config.globalProperties.$appData.userInfo = uni.getStorageSync('userInfo');
    }
    
    // 添加Coze配置作为全局属性
    app.config.globalProperties.$cozeConfig = {
        apiKey: 'pat_gP4vPWxbg1JeFUkfz4KmGJc7HASmwcUg3ewhPGHqVhv81ryfqkfGTSM4aGBbb92l',
        botId: '7493773966524989477'
    };
    
    return {
        app,
    };
}