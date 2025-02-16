<!-- 要用多级菜单就用<menu></menu>这种方式，一级就menuone -->
<template>
    <el-container class="mycontainer">
    <el-aside width="auto" class="asside">
        <MenuOne></MenuOne>
    </el-aside>
    <el-container>
    <el-header class="header">
            <Collapse></Collapse>
            <BreadCrumb></BreadCrumb>
    </el-header>
    <el-main class="mymain">
        <router-view></router-view>
    </el-main>
    </el-container>
    </el-container>
    </template>
    <script setup lang="ts">
    //import Menu from './menu.vue'
    import MenuOne from './MenuOne.vue'
    import Collapse from './Collapse.vue'
    import BreadCrumb from './BreadCrumb.vue'
    </script>
    <style scoped lang="scss">

    .mycontainer{
    height: 776px;
    .asside{
    background-color: #304156;
    }
    .header{
    background-color: #009688;
    color:#FFF;
    display:flex;
    align-items: center;
    }
    .mymain{
        padding:0px;
    }
    }
    </style>