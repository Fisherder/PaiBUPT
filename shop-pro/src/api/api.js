// api/api.js
import { request } from './request.js';


// 基础URL
const BASE_URL = 'http://localhost:8089'; // 根据您的实际后端地址修改

// 商品相关API
export const goodsApi = {
  // 获取商品列表
  getGoodsList(params) {
    return request({
      url: `${BASE_URL}/api/goods/list`,
      method: 'GET',
      data: params
    });
  },
  
  // 获取商品详情
  getGoodsDetail(goodsId) {
    return request({
      url: `${BASE_URL}/api/goods/detail`,
      method: 'GET',
      data: { goodsId }
    });
  }
};

// 搜索和推荐API
export const searchApi = {
  // 搜索商品
  searchGoods(params) {
    return request({
      url: `${BASE_URL}/api/es/search`,
      method: 'GET',
      data: params
    });
  },
  
  // 获取推荐商品
  getRecommendGoods(userId, limit = 6) {
    return request({
      url: `${BASE_URL}/api/es/recommend`,
      method: 'GET',
      data: { userId, limit }
    });
  },
  
  // 获取热门商品
  getHotGoods(limit = 6) {
    return request({
      url: `${BASE_URL}/api/es/hot`,
      method: 'GET',
      data: { limit }
    });
  }
};

// 用户行为API
export const behaviorApi = {
  // 记录浏览行为
  recordView(userId, goodsId) {
    return request({
      url: `${BASE_URL}/api/behavior/view`,
      method: 'POST',
      data: { userId, goodsId }
    });
  },
  
  // 记录收藏行为
  recordFavorite(userId, goodsId) {
    return request({
      url: `${BASE_URL}/api/behavior/favorite`,
      method: 'POST',
      data: { userId, goodsId }
    });
  },
  
  // 记录购买行为
  recordPurchase(userId, goodsId) {
    return request({
      url: `${BASE_URL}/api/behavior/purchase`,
      method: 'POST',
      data: { userId, goodsId }
    });
  }
};

// 用户API
export const userApi = {
  // 用户登录
  login(data) {
    return request({
      url: `${BASE_URL}/api/wxUser/login`,
      method: 'POST',
      data
    });
  },
  
  // 用户注册
  register(data) {
    return request({
      url: `${BASE_URL}/api/wxUser/register`,
      method: 'POST',
      data
    });
  }
};