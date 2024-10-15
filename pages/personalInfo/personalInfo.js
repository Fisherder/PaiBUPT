// pages/personalInfo/personalInfo.js
const app = getApp(); // 获取全局 app 实例

Page({

  /**
   * 页面的初始数据
   */
  data: {
    hasUserInfo: false, // 标记是否已经获取到用户信息
    userInfo: null, // 存储用户信息
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
  goToLogIn(){
    wx.navigateTo({
      url: '/pages/logIn/logIn',
    });
    const that = this;
    wx.login({
      success (res) {
        // if (res.code) {
        //   //发起网络请求
        //   wx.request({
        //     url: 'https://example.com/onLogin',//待补充的后端地址
        //     data: {
        //       code: res.code
        //     }
        //   })
        // } else {
        //   console.log('登录失败！' + res.errMsg)
        // }
        /////////////////////////模拟登录
        that.setData({
          hasUserInfo: true,
          userInfo : true,
          isLoggedIn : true,
          IDGetted : true
          });
      }
    })
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