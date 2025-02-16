//定义用户数据类型:interface和type
export type User = {
    userId:string,
    username:string,
    password:string,
    nickName:string,
    phone:string,
    sex:string,
    status:string

}
//定义列表查询参数
export type ListParm ={
    currentPage:number,
    pageSize:number,
    nickName:string,
    total?:number
}