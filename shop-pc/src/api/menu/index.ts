import http from "../../http";
//获取上级菜单数据
export const getParentApi = ()=>{
    return http.get("/api/menu/getParent")
}