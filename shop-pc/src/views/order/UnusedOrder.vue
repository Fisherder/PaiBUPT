<template>
    <el-main>
    <!-- 搜索栏 -->
    <el-form :model="parm" :inline="true" size="default">
    <el-form-item>
    <el-input v-model="parm.goodsName" placeholder="请输入商品名称"></el-input>
    </el-form-item>
    <el-form-item>
    <el-button @click="searchBtn" icon="Search">搜索</el-button>
    <el-button @click="resetBtn" type="danger" plain icon="Close">重置
    </el-button>
    </el-form-item>
    </el-form>
    <!-- 表格 -->
    <el-table :height="tableHeight" :data="tableList" border stripe>
    <el-table-column label="商品图片" prop="image">
    <template #default="scope">
    <el-image v-if="scope.row.image" style="width: 100px; height:
    60px" :src="scope.row.image.split(',')[0]" />
    </template>
    </el-table-column>
    <el-table-column label="商品名称" prop="goodsName"></el-table-column>
    <!-- <el-table-column label="期望价格" prop="goodsPrice"></el-table-column> -->
    <el-table-column label="交易价格" prop="price"></el-table-column>
    <el-table-column label="购买人" prop="nickName"></el-table-column>
    <el-table-column label="交易时间" prop="createTime"></el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination
    @size-change="sizeChange"
    @current-change="currentChange"
    :current-page.sync="parm.currentPage"
    :page-sizes="[10,20, 40, 80, 100]"
    :page-size="parm.pageSize"
    layout="total, sizes, prev, pager, next, jumper"
    :total="parm.total" background>
    </el-pagination>
    </el-main>
    </template>
    <script setup lang="ts">
    import { nextTick, onMounted, reactive, ref } from 'vue';
    import { getListApi } from '../../api/order';
    //表格高度
    const tableHeight = ref(0)
    //参数
    const parm = reactive({
    currentPage:1,
    pageSize:10,
    goodsName:'',
    total:0
    })
    //表格数据
    const tableList = ref([])
    //获取列表
    const getList = async()=>{
    let res = await getListApi(parm)
    if(res && res.code == 200){
    console.log(res)
    //设置表格数据
    tableList.value = res.data.records;
    parm.total = res.data.total;
    }
    }
    //页容量改变时触发
    const sizeChange = (size:number)=>{
    parm.pageSize = size;
    getList()
    }
    //页数改变时触发
    const currentChange = (page:number)=>{
    parm.currentPage = page;
    getList();
    }
    //搜索
    const searchBtn = ()=>{
    getList()
    }
    //重置
    const resetBtn = ()=>{
    parm.currentPage = 1;
    parm.goodsName = ""
    getList()
    }
    onMounted(()=>{
    nextTick(()=>{
    tableHeight.value = window.innerHeight - 200
    })
    getList()
    })
    </script>
    <style scoped>
    </style>