//定义用户数据类型:interface和type
export type User = {
    userId: string,
    username: string,
    password: string,
    nickName: string,
    phone: string,
    sex: string,
    status: string

}
//定义列表查询参数
export type ListParm = {
    currentPage: number,
    pageSize: number,
    nickName: string,
    total?: number
}
//登录参数
export type LoginParm = {
    username: string,
    password: string,
    code: string
}
//查询树数据的参数类型
export type AssignParm = {
    assId: string,
    userId: string
}
//保存权限的数据类型
export type SaveAssignParm = {
    assId: string,
    list: Array<string>
}