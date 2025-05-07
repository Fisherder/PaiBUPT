import http from "../common/http";
import { request } from "./request";

//首页轮播图数据查询
export const getIndexBannerApi =()=>{
	return http.get('/api/banner/getIndexBanner')
}
//查询首页推荐数据
export const getIndexListApi =(parm)=>{
	return http.get('/api/goods/getIndexList',parm)
}
// 在api.js中添加
export const getCategoryRecommendGoods = (categoryId, limit = 5) => {
    return request({
        url: `${BASE_URL}/api/goods/getCategoryRecommend`,
        method: 'GET',
        data: { categoryId, limit }
    });
};