/**
 * 修改后的地图服务工具类，增加了多点标记和线路展示功能
 */
const MAP_KEY = 'VJWBZ-RNGKV-LBYPK-5ZFCK-5SB6Q-KNFOR'; // 替换为你申请的腾讯地图密钥

// 地理编码 - 将地址转换为经纬度
export const geocoder = (address) => {
  return new Promise((resolve, reject) => {
    if (!address) {
      reject(new Error('地址不能为空'));
      return;
    }
    
    uni.request({
      url: `https://apis.map.qq.com/ws/geocoder/v1/`,
      data: {
        address: address,
        key: MAP_KEY
      },
      success: (res) => {
        if (res.data && res.data.status === 0) {
          const location = res.data.result.location;
          resolve({
            latitude: location.lat,
            longitude: location.lng,
            address: res.data.result.address,
            formatted_address: res.data.result.formatted_addresses?.recommend || res.data.result.address
          });
        } else {
          reject(new Error(`地理编码失败: ${res.data.message || '未知错误'}`));
        }
      },
      fail: (err) => {
        reject(new Error(`地理编码请求失败: ${err.errMsg || JSON.stringify(err)}`));
      }
    });
  });
};

// 逆地理编码 - 将经纬度转换为地址
export const reverseGeocoder = (latitude, longitude) => {
  return new Promise((resolve, reject) => {
    if (!latitude || !longitude) {
      reject(new Error('经纬度不能为空'));
      return;
    }
    
    uni.request({
      url: `https://apis.map.qq.com/ws/geocoder/v1/`,
      data: {
        location: `${latitude},${longitude}`,
        key: MAP_KEY,
        get_poi: 1
      },
      success: (res) => {
        if (res.data && res.data.status === 0) {
          const result = res.data.result;
          resolve({
            address: result.address,
            formatted_address: result.formatted_addresses?.recommend || result.address,
            location: result.location,
            pois: result.pois || []
          });
        } else {
          reject(new Error(`逆地理编码失败: ${res.data.message || '未知错误'}`));
        }
      },
      fail: (err) => {
        reject(new Error(`逆地理编码请求失败: ${err.errMsg || JSON.stringify(err)}`));
      }
    });
  });
};

// 计算两点之间的距离（直线距离，单位：米）
export const calculateDistance = (fromLatitude, fromLongitude, toLatitude, toLongitude) => {
  return new Promise((resolve, reject) => {
    if (!fromLatitude || !fromLongitude || !toLatitude || !toLongitude) {
      reject(new Error('经纬度参数不完整'));
      return;
    }
    
    uni.request({
      url: `https://apis.map.qq.com/ws/distance/v1/`,
      data: {
        from: `${fromLatitude},${fromLongitude}`,
        to: `${toLatitude},${toLongitude}`,
        key: MAP_KEY
      },
      success: (res) => {
        if (res.data && res.data.status === 0) {
          const distance = res.data.result.elements[0].distance;
          resolve(distance); // 返回距离，单位：米
        } else {
          reject(new Error(`距离计算失败: ${res.data.message || '未知错误'}`));
        }
      },
      fail: (err) => {
        reject(new Error(`距离计算请求失败: ${err.errMsg || JSON.stringify(err)}`));
      }
    });
  });
};

// 获取当前位置
export const getCurrentLocation = () => {
  return new Promise((resolve, reject) => {
    uni.getLocation({
      type: 'gcj02', // 使用国测局坐标系
      success: (res) => {
        resolve({
          latitude: res.latitude,
          longitude: res.longitude
        });
      },
      fail: (err) => {
        reject(new Error(`获取位置失败: ${err.errMsg || JSON.stringify(err)}`));
      }
    });
  });
};

// 获取两点之间的路线规划
export const getDirections = (fromLat, fromLng, toLat, toLng, mode = 'driving') => {
  return new Promise((resolve, reject) => {
    uni.request({
      url: `https://apis.map.qq.com/ws/direction/v1/${mode}`,
      data: {
        from: `${fromLat},${fromLng}`,
        to: `${toLat},${toLng}`,
        key: MAP_KEY
      },
      success: (res) => {
        if (res.data && res.data.status === 0) {
          resolve(res.data.result);
        } else {
          reject(new Error(`获取路线失败: ${res.data.message || '未知错误'}`));
        }
      },
      fail: (err) => {
        reject(new Error(`路线请求失败: ${err.errMsg || JSON.stringify(err)}`));
      }
    });
  });
};

// 解析路线坐标点
export const parsePolyline = (polyline) => {
  if (!polyline) return [];
  
  const points = [];
  const len = polyline.length;
  
  for (let i = 0; i < len; i += 2) {
    points.push({
      latitude: polyline[i],
      longitude: polyline[i + 1]
    });
  }
  
  return points;
};

// 格式化距离
export const formatDistance = (distance) => {
  if (distance < 1000) {
    return `${Math.round(distance)}米`;
  } else {
    return `${(distance / 1000).toFixed(1)}公里`;
  }
};

// 计算地图缩放级别使两点都能显示
export const calculateZoomLevel = (lat1, lng1, lat2, lng2) => {
  // 简单算法，基于两点距离估算合适的缩放级别
  const distance = Math.sqrt(Math.pow(lat1 - lat2, 2) + Math.pow(lng1 - lng2, 2));
  
  // 距离越大，缩放级别越小
  if (distance > 0.1) return 10;
  if (distance > 0.05) return 12;
  if (distance > 0.01) return 14;
  if (distance > 0.005) return 15;
  return 16;
};

export default {
  geocoder,
  reverseGeocoder,
  calculateDistance,
  getCurrentLocation,
  getDirections,
  parsePolyline,
  formatDistance,
  calculateZoomLevel,
  MAP_KEY
};