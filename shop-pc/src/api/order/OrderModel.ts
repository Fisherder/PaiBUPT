//定义列表查询的参数
export type ListParm = {
    currentPage:number,
    pageSize:number,
    goodsName:string,
    total?:number
    }