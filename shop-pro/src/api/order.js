import http from "../common/http";
//提交报价
export const replaceOrderApi =(parm)=>{
	return http.post('/api/goodsOrder/replaceOrder', parm)
}
//查询信息
export const getGoodsDetailApi =(parm)=>{
	return http.get('/api/goodsOrder/getGoodsDetail',parm)
}