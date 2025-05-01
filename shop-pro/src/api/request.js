// api/request.js

/**
 * 封装uni-app请求
 * @param {Object} options - 请求选项
 * @returns {Promise} - 返回Promise对象
 */
export const request = (options) => {
  return new Promise((resolve, reject) => {
    // 获取用户信息，从本地存储中取token
    const userInfo = uni.getStorageSync('userInfo') || {};
    const token = userInfo.token || '';
    
    // 显示加载中
    uni.showLoading({
      title: '加载中...'
    });
    
    uni.request({
      ...options,
      header: {
        'content-type': 'application/json',
        'Authorization': token,
        ...options.header
      },
      success: (res) => {
        // 判断请求是否成功（根据您的API返回结构调整）
        if (res.data.code === 200) {
          resolve(res.data);
        } else {
          // 处理业务错误
          uni.showToast({
            title: res.data.msg || '请求失败',
            icon: 'none'
          });
          reject(res.data);
        }
      },
      fail: (err) => {
        uni.showToast({
          title: '网络异常，请检查网络连接',
          icon: 'none'
        });
        reject(err);
      },
      complete: () => {
        uni.hideLoading();
      }
    });
  });
};

/**
 * 上传文件
 * @param {String} filePath - 文件路径
 * @returns {Promise} - 返回Promise对象
 */
export const uploadFile = (filePath) => {
  return new Promise((resolve, reject) => {
    const userInfo = uni.getStorageSync('userInfo') || {};
    const token = userInfo.token || '';
    
    uni.showLoading({
      title: '上传中...'
    });
    
    uni.uploadFile({
      url: 'http://localhost:8089/api/upload/uploadImage',
      filePath: filePath,
      name: 'file',
      header: {
        'Authorization': token
      },
      success: (res) => {
        const data = JSON.parse(res.data);
        if (data.code === 200) {
          resolve(data);
        } else {
          uni.showToast({
            title: data.msg || '上传失败',
            icon: 'none'
          });
          reject(data);
        }
      },
      fail: (err) => {
        uni.showToast({
          title: '上传失败，请重试',
          icon: 'none'
        });
        reject(err);
      },
      complete: () => {
        uni.hideLoading();
      }
    });
  });
};