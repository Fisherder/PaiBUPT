import http from "../../http";
import type { ListParm, User } from "./UserModel";
//新增
export const addAdminUserApi = (parm:User)=>{
    return http.post("/api/sysUser",parm)
}
//列表
export const getListApi = (parm:ListParm)=>{
    return http.get("/api/sysUser/getList",parm)//请求getList这个方法
}
//编辑
export const editApi = (parm:User)=>{
    return http.put("/api/sysUser",parm)
}
//删除
export const deleteApi = (userId:string)=>{
    return http.delete(`/api/sysUser/${userId}`)
}