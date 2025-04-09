import http from "../common/http";
//注册
export const registerApi = (parm) => {
	return http.post('/api/wxUser/register', parm)
}
//登录
export const loginApi = (parm) => {
	return http.post('/api/wxUser/login', parm)
}
//修改密码
export const wxupdatePasswordApi = (parm) => {
	return http.post('/api/wxUser/wxupdatePassword', parm)
}
//修改个人信息
export const editInfoApi = (parm) => {
	return http.post('/api/wxUser/editInfo', parm)
}
//查询个人信息
export const getInfoApi = (parm) => {
	return http.get('/api/wxUser/getInfo', parm)
}
//忘记密码
export const forgetApi = (parm) => {
	return http.post('/api/wxUser/forget', parm)
}