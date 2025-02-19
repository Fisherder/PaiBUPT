import { defineStore } from "pinia";
import type { PersistenceOptions } from 'pinia-plugin-persistedstate'
export const userStore = defineStore('userStore', {
    state: () => {
        return {
            userId: '',
            nickName: "",
            menuList:[],
            codeList:[]
        }
    },
    //获取值
    getters: {
        getUserId(state) {
            return state.userId
        },
        getMenuList(state) {
            return state.menuList
        },
        getCodeList(state) {
            return state.codeList
        }
    },
    //改变state的值
    actions: {
        setUserId(userId: string) {
            this.userId = userId;
        },
        setMenuList(menuList: any) {
            this.menuList = menuList;
        },
        setCodeList(codeList: any) {
            this.codeList = codeList;
        }
    },
    persist: {
        enabled: true,
        strategies: [
            { storage: sessionStorage, paths: ['userId', 'nickName','menuList','codeList'] },
        ]
    } as PersistenceOptions
})
