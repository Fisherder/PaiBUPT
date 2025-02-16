<template>
        <el-icon size="30" style="cursor:pointer;" @click="iconClick">
            <!-- 动态组件：动态切换组件 -->
             <component :is="status ? Expand : Fold"></component>
        </el-icon>
    
</template>

<script setup lang="ts">
import { collapseStore } from '../store/collapse/index'
import { computed } from 'vue';
import {Expand,Fold} from '@element-plus/icons-vue'
const store=collapseStore()
//切换菜单的属性
const status = computed(()=>{
    return store.getCollapse
})
//图标点击事件
const iconClick = ()=>{
    store.setCollapse(!store.collapse)
}
</script>

<style scoped>

</style>