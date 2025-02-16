<template>
   <el-breadcrumb class="bred" separator="/">
    <el-breadcrumb-item v-for="item in tabs">{{ item.meta.title }}</el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script setup lang="ts">
import { onMounted, ref, type Ref, watch } from "vue";
import { useRoute,type RouteLocationMatched } from 'vue-router';
//当前路由
const route =useRoute();
//定义面包屑导航数据
//ref用于定义简单类型显示数据，Ref定义数据类型
const tabs : Ref<RouteLocationMatched[]> = ref([]);
  watch(
  //监听当前路由
  ()=>route.path,
  ()=>getBreadCrumb()
);
//获取面包数据
const getBreadCrumb = ()=>{
  //找出存在title属性的路由数据
  let matched = route.matched.filter((item)=>item.meta && item.meta.title);
  const first=matched[0]
  if(first.path!=='/dashboard'){
    //构造首页
    matched=[{path:'/dashboard',meta:{title:'首页'}}as any].concat(matched)
  }
  tabs.value=matched;
};

onMounted(()=>{
  getBreadCrumb()
})
</script>

<style scoped lang="scss">
//修改字体颜色
:deep(.el-breadcrumb__inner) {
color: #fff !important;
}
.bred {
margin-left: 20px;
}
// 修改字体大小
:deep(.el-breadcrumb__item) {
font-size: 15px !important;
}
</style>