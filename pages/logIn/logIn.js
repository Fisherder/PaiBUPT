// pages/logIn/logIn.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    nickName: '',
    phoneNumber: '',
    isPhoneError: false,        // 是否显示手机号错误
    phoneErrorMessage: '',      // 手机号错误提示
  },
  // 监听手机号输入
  onPhoneInput(event) {
    const phone = event.detail; // 获取输入的手机号
    const phoneRegex = /^[1][3-9][0-9]{9}$/;  // 常用的手机号正则表达式

    // 校验手机号格式
    if (!phoneRegex.test(phone)) {
      this.setData({
        isPhoneError: true,            // 显示错误
        phoneErrorMessage: '手机号格式错误', // 错误提示信息
        phone: phone
      });
    } else {
      this.setData({
        isPhoneError: false,           // 隐藏错误
        phoneErrorMessage: '',         // 清空错误信息
        phone: phone
      });
    }
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