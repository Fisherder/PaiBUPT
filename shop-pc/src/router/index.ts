//一级菜单的方式
import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import Layout from '@/layout/Index.vue'
const routes: Array<RouteRecordRaw> = [
{
path: '/',
component: Layout,
redirect: '/dashboard',
children: [
{
path: '/dashboard',
component: () => import('@/layout/dashboard.vue'),
name: 'dashboard',
meta: {
title: '首页',
icon: '#icondashboard'
}
},
{
path: "/adminUser",
component: () => import('@/views/system/AdminUser.vue'),
name: "adminUser",
meta: {
title: "管理员管理",
icon: "UserFilled",
roles: ["sys:adminUser"],
}
},
{
path: "/userList",
component: () => import('@/views/system/UserList.vue'),
name: "userList",
meta: {
title: "用户管理",
icon: "Wallet",
roles: ["sys:userList"],
}
},
{
path: "/menuList",
component: () => import('@/views/menu/Index.vue'),
name: "menuList",
meta: {
title: "菜单管理",
icon: "Menu",
roles: ["sys:menu"],
}
},
{
path: "/goodsType",
component: () => import('@/views/goods/GoodsType.vue'),
name: "goodsType",
meta: {
title: "商品分类",
icon: "UserFilled",
roles: ["sys:goodsType"],
}
},
{
path: "/unusedList",
component: () => import('@/views/goods/UnusedList.vue'),
name: "unusedList",
meta: {
title: "商品管理",
icon: "Wallet",
roles: ["sys:unusedList"],
}
},
{
path: "/unusedOrder",
component: () => import('@/views/order/UnusedOrder.vue'),
name: "unusedOrder",
meta: {
title: "闲置订单",
icon: "UserFilled",
roles: ["sys:unusedOrder"],
}
},
{
path: "/bannerList",
component: () => import('@/views/banner/Index.vue'),
name: "bannerList",
meta: {
title: "广告列表",
icon: "UserFilled",
roles: ["sys:bannerList"],
}
},
{
    path: "/commentList",
    component: () => import('@/views/comment/CommentList.vue'),
    name: "commonList",
    meta: {
    title: "评论列表",
    icon: "UserFilled",
    roles: ["sys:commentList"],
    }
    }
]
}
]
const router = createRouter({
history: createWebHistory(),
routes
})
export default router