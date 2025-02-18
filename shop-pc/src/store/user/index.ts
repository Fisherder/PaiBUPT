import { defineStore } from "pinia";
//创建store
//defineStore后第一个参数：唯一的不能重复
export const userStore=defineStore('userStore',{
    state:()=>{
        return{
            userId:'',
            nickName:""
        }
    },
    //获取state值
    getters:{
        getUserId(state){
            return state.userId
        }
    },
    //改变state的值
    actions:{
        setUserId(userId:string){
            this.userId=userId;
        }
    }
})

