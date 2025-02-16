//定义列表查询参数
export type WxUserListParm ={
    currentPage:number,
    pageSize:number,
    phone:string,
    total?:number
}
//用户数据类型
export type WxUser ={
    userId:string,
    nickName?:string,
    name?:string,
    picture?:string,
    phone?:string,
    username?:string,
    status?:string
}