import http from "../common/http";
//发布分类
export const categoryApi = (parm) => {
	return http.get('/api/category/getSelectList', parm)
}
//发布商品
export const releaseApi = (parm) => {
	return http.post('/api/goods/release', parm)
}
//闲置分类
export const getCateListApi = () => {
	return http.get('/api/category/getCateList')
}
//闲置列表
export const getUsedListApi = (parm) => {
	return http.get('/api/goods/getUsedList', parm)
}
//求购列表
export const getBuyListApi = (parm) => {
	return http.get('/api/goods/getBuyList', parm)
}
//我的闲置列表
export const getMyUnusedListApi = (parm) => {
	return http.get('/api/goods/getMyUnusedList', parm)
}
//我的闲置列表
export const editApi = (parm) => {
	return http.post('/api/goods/edit', parm)
}
//下架
export const upanddownApi = (parm) => {
	return http.post('/api/goods/upanddown', parm)
}
//删除
export const deleteApi = (parm) => {
	return http.post('/api/goods/delete', parm)
}
//收藏列表
export const getMyCollectApi = (parm) => {
	return http.get('/api/collect/getMyCollect', parm)
}
//取消收藏
export const cancelCollectApi = (parm) => {
	console.log(parm)
	return http.post('/api/collect/cancel', parm)
}