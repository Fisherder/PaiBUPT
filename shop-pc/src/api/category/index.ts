import http from "../../http";
import type { Category, CategoryListParm } from "./CategoryModel";

//新增
export const addCategoryApi = (parm:Category)=>{
    return http.post("/api/category",parm)
}
//列表
export const listApi = (parm:CategoryListParm)=>{
    return http.get("/api/category/list",parm)
}
//编辑
export const editCategoryApi = (parm:Category)=>{
    return http.put("/api/category",parm)
}
//删除
export const deleteCategoryApi = (parm:Category)=>{
    return http.delete(`/api/category/${parm.categoryId}`)
}