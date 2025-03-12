import http from "../common/http"
export const collectApi = (parm) => {
	return http.post('/api/collect/collect', parm)
}
//是否收藏
export const hasCollectApi = (parm) => {
	return http.get('/api/collect/hasCollect', parm)
}