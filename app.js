// app.js
App({
  globalData: {
    userInfo: null,
    isLoggedIn :false
  },
  onLaunch() {
    // // 展示本地存储能力
    // const logs = wx.getStorageSync('logs') || []
    // logs.unshift(Date.now())
    // wx.setStorageSync('logs', logs)

    // // 登录
    // wx.login({
    //   success: res => {
    //     // 发送 res.code 到后台换取 openId, sessionKey, unionId
    //   }
    // })
    // 展示本地存储能力：记录启动时间
    const logs = wx.getStorageSync('logs') || [];
    logs.unshift(Date.now());
    wx.setStorageSync('logs', logs);

    // 检查是否已有登录数据
    const storedUserInfo = wx.getStorageSync('userInfo');
    if (storedUserInfo) {
      this.globalData.userInfo = storedUserInfo;
      this.globalData.isLoggedIn = true;
      console.log('用户已登录:', storedUserInfo);
    } else {
      // 未登录时，引导用户进行登录
      this.performLogin();
    }
  },

  // 执行登录并引导授权获取用户信息
  performLogin() {
    // 检查本地是否已经有用户信息
    const storedUserInfo = wx.getStorageSync('userInfo');
    if (storedUserInfo) {
      this.globalData.userInfo = storedUserInfo;
      this.globalData.isLoggedIn = true;
      console.log('用户已登录:', storedUserInfo);
    } else {
      console.log('用户未登录，需要用户主动登录');
    }
  }  
})
