import { defineStore } from "pinia";
//创建store
//defineStore后第一个参数：唯一的不能重复
export const testStore=defineStore('testStore',{
    state:()=>{
        return{
            count:0
        }
    },
    //获取state值
    getters:{
        getCount(state){
            return state.count
        }
    },
    //改变state的值
    actions:{
        setCount(count:number){//count,number类型 
            this.count=count;
        }
    }
})

