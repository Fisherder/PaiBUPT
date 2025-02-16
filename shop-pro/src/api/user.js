import http from "../common/http";
//注册
export const registerApi =(parm)=>{
	return http.post('/api/wxUser/register', parm)
}
//登录
export const loginApi =(parm)=>{
	return http.post('/api/wxUser/login', parm)
}