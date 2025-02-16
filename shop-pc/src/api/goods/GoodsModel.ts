//定义列表查询参数
export type GoodsListParm ={
    currentPage:number,
    pageSize:number,
    goodsName:string,
    total?:number
}
//上下架数据类型
export type UpdateParm ={
   goodsId:string,
   status?:string,
   setIndex?:string//?代表是可选参数
}