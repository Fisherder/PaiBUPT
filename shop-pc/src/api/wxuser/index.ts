import http from "../../http";
//管理用户
import type { WxUser, WxUserListParm } from "./WxUserModel";
//查询列表
export const getListApi=(parm:WxUserListParm)=>{
    return http.get("/api/wxUser/list",parm)
}
//停用
export const stopUserApi=(parm:WxUser)=>{
    return http.post("/api/wxUser/stopUser",parm)
}
//重置密码
export const updatePasswordApi=(parm:WxUser)=>{
    return http.post("/api/wxUser/updatePassword",parm)
}
//删除
export const deleteApi=(parm:WxUser)=>{
    return http.delete(`/api/wxUser/${parm.userId}`)
}