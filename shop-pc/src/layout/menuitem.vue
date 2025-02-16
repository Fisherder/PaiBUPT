<template>
    <!-- 有children走上无children走下 -->
    <!-- index作为路由参数 -->
    <!-- 接收父组件menu传递的menuList参数 -->
    <template v-for="menu in menuList" :key="menu.path" >
        <el-sub-menu v-if="menu.children && menu.children.length > 0" :index="menu.path">
      <template #title>
        <el-icon>
            <!-- component动态组件动态生成图标 -->
            <!-- 动态组件 -->
            <component :is="menu.meta.icon"></component>
        </el-icon>
        <!-- 标题 -->
        <span>{{menu.meta.title}}</span>
      </template>
     <!-- 递归调用生成下级菜单 -->
      <menuitem :menuList="menu.children"></menuitem>
    </el-sub-menu>
    <el-menu-item  style="color: #f4f4f5" v-else :index="menu.path">
      <el-icon>
         <!-- 动态组件 -->
         <component :is="menu.meta.icon"></component>
      </el-icon>
      <template #title>{{menu.meta.title}}</template>
    </el-menu-item>
</template>
</template>

<script setup lang="ts">
import MenuItem from 'element-plus/es/components/menu/src/utils/menu-item.mjs';


defineProps(['menuList'])
</script>

<style scoped>

</style>