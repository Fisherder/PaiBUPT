//定义分类数据类型
export type Category ={
    categoryId:string,
    categoryName:string,
    orderNum:string
}
//定义列表查询参数
export type CategoryListParm ={
    currentPage:number,
    pageSize:number,
    categoryName:string,
    total?:number
}