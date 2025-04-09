import http from "../common/http";
//提交报价
export const replaceOrderApi = (parm) => {
	return http.post('/api/goodsOrder/replaceOrder', parm)
}
//查询信息
export const getGoodsDetailApi = (parm) => {
	return http.get('/api/goodsOrder/getGoodsDetail', parm)
}
//我的订单
export const getMyOrderApi = (parm) => {
	return http.get('/api/goodsOrder/getMyOrder', parm)
}
//出售订单
export const getSellOrderApi = (parm) => {
	return http.get('/api/goodsOrder/getSellOrder', parm)
}
//删除
export const deleteOrderApi = (parm) => {
	return http.post('/api/goodsOrder/deleteOrder', parm)
}