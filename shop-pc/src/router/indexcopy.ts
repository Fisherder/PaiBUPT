//多级菜单的方式
import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import Layout from '@/layout/index.vue'
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
            }
        ]
    },
    {
        path: "/system",
        component: Layout,
        name: "system",
        meta: {
            title: "系统管理",
            icon: "Setting",
            roles: ["sys:manage"],
        },
        children: [
            {
                path: "/adminUser",
                component: () => import('@/views/system/AdminUser.vue'),
                name: "adminUser",
                meta: {
                    title: "管理员管理",
                    icon: "UserFilled",
                    roles: ["sys:adminUser"],
                },
            },
            {
                path: "/userList",
                component: () => import('@/views/system/UserList.vue'),
                name: "userList",
                meta: {
                    title: "用户管理",
                    icon: "Wallet",
                    roles: ["sys:userList"],
                },
            },
            {
                path: "/menuList",
                component: () => import('@/views/system/MenuList.vue'),
                name: "menuList",
                meta: {
                    title: "菜单管理",
                    icon: "Menu",
                    roles: ["sys:menu"],
                },
            },
        ],
    },
    {
        path: "/goodsRoot",
        component: Layout,
        name: "goodsRoot",
        meta: {
            title: "商品管理",
            icon: "Setting",
            roles: ["sys:goodsRoot"],
        },
        children: [
            {
                path: "/goodsType",
                component: () => import('@/views/goods/GoodsType.vue'),
                name: "goodsType",
                meta: {
                    title: "商品分类",
                    icon: "UserFilled",
                    roles: ["sys:goodsType"],
                },
            },
            {
                path: "/unusedList",
                component: () => import('@/views/goods/UnusedList.vue'),
                name: "unusedList",
                meta: {
                    title: "闲置商品",
                    icon: "Wallet",
                    roles: ["sys:unusedList"],
                },
            },
            {
                path: "/buyList",
                component: () => import('@/views/goods/BuyList.vue'),
                name: "buyList",
                meta: {
                    title: "求购商品",
                    icon: "Wallet",
                    roles: ["sys:buyList"],
                },
            },
        ],
    },
    {
        path: "/order",
        component: Layout,
        name: "order",
        meta: {
            title: "订单管理",
            icon: "Setting",
            roles: ["sys:order"],
        },
        children: [
            {
                path: "/unusedOrder",
                component: () => import('@/views/order/UnusedOrder.vue'),
                name: "unusedOrder",
                meta: {
                    title: "闲置订单",
                    icon: "UserFilled",
                    roles: ["sys:unusedOrder"],
                },
            },
            {
                path: "/buyOrder",
                component: () => import('@/views/order/BuyOrder.vue'),
                name: "buyOrder",
                meta: {
                    title: "求购订单",
                    icon: "Wallet",
                    roles: ["sys:buyOrder"],
                },
            },
        ],
    },
    {
        path: "/comment",
        component: Layout,
        name: "comment",
        meta: {
            title: "评论管理",
            icon: "Setting",
            roles: ["sys:comment"],
        },
        children: [
            {
                path: "/commentList",
                component: () => import('@/views/comment/CommentList.vue'),
                name: "commentList",
                meta: {
                    title: "评论列表",
                    icon: "UserFilled",
                    roles: ["sys:commentList"],
                },
            },
        ],
    }
]
const router = createRouter({
    history: createWebHistory(),
    routes
})
export default router