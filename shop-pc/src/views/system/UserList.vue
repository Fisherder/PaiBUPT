<template>
   <el-main>
         <!-- 搜索栏 -->
    <el-form :model="searchParm" :inline="true" size="default">
        <el-form-item>
            <el-input
                placeholder="请输入电话"
                v-model="searchParm.phone"
                >
            </el-input>
        </el-form-item>
        <el-form-item>
            <el-button @click="searchBtn" icon="Search">搜索</el-button>
            <el-button @click="closeBtn" type="danger" plain icon="Close">重置</el-button>
        </el-form-item>
    </el-form>
    <!-- 表格 -->
    <el-table :height="tableHeight" :data="tableList" border stripe>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="nickName" label="昵称"></el-table-column>
        <el-table-column prop="phone" label="电话"></el-table-column>
        <el-table-column prop="username" label="账户"></el-table-column>
        <el-table-column prop="status" label="停用">
            <!-- 0不停用，1停用 -->
            <template #default="scope">
                <el-switch
                    v-model="scope.row.status"
                    :active-value="'0'"
                    :inactive-value="'1'"
                    :before-change="beforeStatus"
                    @change="changeStatus(scope.row.status, scope.row.userId)"/>
            </template>
        </el-table-column>
        <el-table-column  label="操作" align="center" width="250">
            <template #default="scope">
                <el-button icon="Setting" type="success" size="default" @click="resetPas(scope.row)">重置密码</el-button>
                <el-button icon="Delete" type="danger" size="default" @click="deleteBtn(scope.row)">删除</el-button>
            </template>
        </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination
    @size-change="sizeChange"
    @current-change="currentChange"
    :current-page.sync="searchParm.currentPage"
    :page-sizes="[10, 20, 40, 80, 100]"
    :page-size="searchParm.pageSize"
    layout="total, sizes, prev, pager, next, jumper"
    :total="searchParm.total"
    background
    >
</el-pagination>
   </el-main>
</template>

<script setup lang="ts">
import { nextTick, onMounted, reactive, ref } from 'vue';
import { deleteApi, getListApi, stopUserApi, updatePasswordApi } from '../../api/wxuser';
import useInstance from '../../hooks/useInstance';
import type { WxUser } from '../../api/wxuser/WxUserModel';
import { ElMessage } from 'element-plus';
//获取全局属性
const { global } = useInstance();
//搜索参数
const searchParm=reactive({
        currentPage:1,
        pageSize:10,
        phone:"",
        total:0
    })
//表格数据
const tableList=ref([
    
])
//获取表格数据
const getList=async()=>{
    let res=await getListApi(searchParm)
    if(res && res.code==200){
        //console.log(res)
        tableList.value=res.data.records;
        searchParm.total=res.data.total;
    }
}
//搜索
const searchBtn=()=>{
    getList()
}
//重置
const closeBtn=()=>{
    searchParm.phone=''
    getList()
}
//上架下架操作之前的事件
const beforeStatus = async () => {
        let confirm = await global.$myconfirm("确定停用吗？");
        return new Promise((resolve, reject) => {
        if (confirm) {
            return resolve(confirm);
        } else {
            return reject(confirm);
    }
    });
    };
    //停用操作
    const changeStatus = async (type: string, goodsId: string) => {
        console.log(type);
        console.log(goodsId);
        let res = await stopUserApi({
            userId: goodsId,
            status: type,
    });
        if (res && res.code == 200) {
            getList();
    }
    };
//重置密码
const resetPas=async(row:WxUser)=>{
    console.log(row)
    const confirm=await global.$myconfirm('确定重置密码吗？重置后密码为666666')
    if(confirm){
        let res=await updatePasswordApi({
            userId:row.userId
        })
        if(res && res.code==200){
            ElMessage.success(res.msg)
            getList()
        }
    }
}
//删除
const deleteBtn=async(row:WxUser)=>{
    console.log(row)
    const confirm=await global.$myconfirm('确定删除该数据吗？')
    if(confirm){
        let res=await deleteApi({
            userId:row.userId
        })
        if(res && res.code==200){
            ElMessage.success(res.msg)
            getList()
        }
    }
}
//页容量改变时触发
const sizeChange = (size:number)=>{
    console.log(size);
    searchParm.pageSize=size;
    getList()
}
//页数改变时触发
const currentChange = (page:number)=>{
    console.log(page)
    searchParm.currentPage=page;
    getList()
}
//表格高度
const tableHeight=ref(0)
//在组件完成初始渲染后触发
onMounted(()=>{
    //nextTick在DOM更新后调用
    nextTick(()=>{
        tableHeight.value=window.innerHeight-220
    })
    getList()
})
</script>

<style scoped>

</style>