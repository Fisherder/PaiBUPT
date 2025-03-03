import http from "../common/http";
//发布分类
export const categoryApi =(parm)=>{
	return http.get('/api/category/getSelectList', parm)
}
//发布商品
export const releaseApi =(parm)=>{
	return http.post('/api/goods/release', parm)
}
//闲置分类
export const getCateListApi =()=>{
	return http.get('/api/category/getCateList')
}
//闲置列表
export const getUsedListApi =(parm)=>{
	return http.get('/api/goods/getUsedList',parm)
}
//求购列表
export const getBuyListApi =(parm)=>{
	return http.get('/api/goods/getBuyList',parm)
}