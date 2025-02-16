<template>
  <div>{{ count }}</div>
  <el-button icon="Edit">编辑</el-button>
    <el-button @click="addBtn" icon="Plus" type="success">新增</el-button>
    <el-button type="info">Info</el-button>
    <el-button type="warning">Warning</el-button>
    <el-button icon="Close" type="danger">Danger</el-button>
    <el-icon >
      <Edit />
    </el-icon>

</template>

<script setup lang="ts">
import { computed} from 'vue';

import { testStore } from '../store/test';
//获取store
const store=testStore()
//从store里获取count
const count = computed(()=>{
  return store.getCount
})
//改变store里的值
const addBtn=()=>{
  store.setCount(++store.count)
}
</script>
<style scoped>
.read-the-docs {
  color: #888;
}
</style>
