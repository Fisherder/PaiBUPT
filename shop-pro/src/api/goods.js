import http from "../common/http";
//发布分类
export const categoryApi =(parm)=>{
	return http.get('/api/category/getSelectList', parm)
}
//发布商品
export const releaseApi =(parm)=>{
	return http.post('/api/goods/release', parm)
}