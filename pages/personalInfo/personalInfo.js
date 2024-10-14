// pages/personalInfo/personalInfo.js
const app = getApp(); // 获取全局 app 实例

Page({

  /**
   * 页面的初始数据
   */
  data: {
    hasUserInfo: false, // 标记是否已经获取到用户信息
    userInfo: null, // 存储用户信息
    openid: '' // 模拟的 openid
  },
  onLoad() {
    if (app.globalData.userInfo) {
      this.setData({
        hasUserInfo: true,
        userInfo: app.globalData.userInfo,
        openid: app.globalData.openid
      });
    }
  },
  logIn (){
    const that = this;
    wx.getUserInfo({
      withCredentials: true,
      success: (result) => {
        console.log(result);
      },
      fail: (res) => {},
      complete: (res) => {},
    })
    // // 第一步：调用 wx.login 获取临时登录凭证（code）
    // wx.login({
    //   success(res) {
    //     if (res.code) {
    //       console.log('获取到的 code:', res.code);
          
    //       // 模拟获取后端返回的 openid
    //       setTimeout(() => {
    //         const fakeOpenid = 'fake_openid_' + res.code; // 模拟的 openid
    //         console.log('模拟后端返回的 openid:', fakeOpenid);

    //         // 将 openid 存储到全局变量
    //         app.globalData.openid = fakeOpenid;

    //         // 第二步：获取用户的公开信息
    //         wx.getUserProfile({
    //           desc: '用于展示用户信息', // 授权说明
    //           success(userRes) {
    //             console.log('获取到的用户信息:', userRes.userInfo);

    //             // 更新页面数据，存储用户信息
    //             that.setData({
    //               hasUserInfo: true,
    //               userInfo: userRes.userInfo
    //             });

    //             // 将用户信息存储到全局变量
    //             app.globalData.userInfo = userRes.userInfo;

    //             // 模拟将 openid 和用户信息发送到后端存储
    //             // 实际开发中需要调用 wx.request 将数据传到后端进行存储
    //             /*
    //             wx.request({
    //               url: 'https://your-backend-server.com/saveUserInfo', // 替换为实际的后端地址
    //               method: 'POST',
    //               data: {
    //                 openid: fakeOpenid,
    //                 userInfo: userRes.userInfo
    //               },
    //               success(saveRes) {
    //                 console.log('用户信息保存成功:', saveRes.data);
    //               }
    //             });
    //             */
    //           },
    //           fail() {
    //             console.log('用户拒绝授权');
    //           }
    //         });
    //       }, 1000); // 模拟网络请求的延迟
    //     } else {
    //       console.log('登录失败！' + res.errMsg);
    //     }
    //   }
    // });
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})