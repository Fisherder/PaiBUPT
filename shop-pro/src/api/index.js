import http from "../common/http";
//首页轮播图数据查询
export const getIndexBannerApi =()=>{
	return http.get('/api/banner/getIndexBanner')
}
//查询首页推荐数据
export const getIndexListApi =(parm)=>{
	return http.get('/api/goods/getIndexList',parm)
}