import http from "../../http";
import type { ListParm, User,LoginParm, AssignParm, SaveAssignParm } from "./UserModel";

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
//验证码
export const imageApi = ()=>{
    return http.post("/api/sysUser/image")
}
//登录
export const loginApi = (parm:LoginParm)=>{
    return http.post("/api/sysUser/login",parm)
}
//获取树的数据
export const getAssignTreeApi = (parm:AssignParm)=>{
    return http.get("/api/menu/getAssignTree",parm)
}
//保存分配的菜单
export const assignSaveApi = (parm:SaveAssignParm)=>{
    return http.post("/api/menu/assignSave",parm)
}