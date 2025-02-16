<template>
    <div class="logo">
        <img :src="MenuLogo"/>
        <span v-if="show" class="logo-title">{{ title }}</span>
    </div>
</template>

<script setup lang="ts">
import MenuLogo from '@/assets/MenuLogo.png'
import {watch, ref } from 'vue';
import { collapseStore } from '../store/collapse/index'
const store=collapseStore()
const title=ref('邮拍')
// const show = computed(()=>{
//     return store.getCollapse
// })
const show=ref(true)
watch(//监视，导航栏展开延时，300是延时时间
    ()=>store.getCollapse,
    (collapsed:boolean)=>{
        if(!collapsed){
            setTimeout(()=>{
                show.value=!collapsed
            },300);
        }else{
            show.value=!collapsed
        }
    }
)
</script>

<style scoped lang="scss">
.logo{
    display: flex;
    width: 100%;
    height: 60px;
    line-height: 60px;
    background-color: #304156;
    cursor:pointer;
    align-items: center;
    img{
        width:50px;
        height:50px;
        margin-left:20px;
    }
    .logo-title{
        color:#bfcbd9;
        font-weight:600;
        font-size:22px;
        font-family: FangSong;
    }
}
</style>