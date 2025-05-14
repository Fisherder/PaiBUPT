import http from "../../http";
import { type ListParm } from "./OrderModel";

//订单列表
export const getListApi = (parm: ListParm) => {
    // 获取userId并确保非null
    const userId = sessionStorage.getItem('userId') || '';
    if (userId) {
        parm.userId = userId;
    }
    return http.get("/api/goodsOrder/getList", parm);
}