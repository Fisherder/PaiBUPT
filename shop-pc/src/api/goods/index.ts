import http from "../../http";
import { type GoodsListParm, type UpdateParm } from "./GoodsModel";
//列表
export const getGoodsListApi = (parm:GoodsListParm)=>{
    return http.get("/api/goods/list",parm)
}
//上下架
export const upanddownApi = (parm:UpdateParm)=>{
    return http.post("/api/goods/upanddown",parm)
}
//推荐首页
export const setIndexApi = (parm:UpdateParm)=>{
    return http.post("/api/goods/setIndex",parm)
}
//删除
export const deleteApi = (parm:UpdateParm)=>{
    return http.post("/api/goods/delete",parm)
}