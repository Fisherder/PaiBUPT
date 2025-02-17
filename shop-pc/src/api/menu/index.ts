import http from "../../http";
import type{  Menu } from "./MenuModel";
//获取上级菜单数据
export const getParentApi = ()=>{
    return http.get("/api/menu/getParent")
}
//新增
export const addApi = (parm:Menu)=>{
    return http.post("/api/menu",parm)
}
//表格列表
export const listApi = ()=>{
    return http.get("/api/menu/list")
}
//编辑
export const editApi = (parm:Menu)=>{
    return http.put("/api/menu",parm)
}
//删除
export const deleteApi = (parm:Menu)=>{
    return http.delete(`/api/menu/${parm.menuId}`)
}