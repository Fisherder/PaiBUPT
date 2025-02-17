<template>
    <!-- 菜单管理，不同管理员可调用的菜单不同 -->
    <el-main style="padding: 15px 30px 20px 30px;">
        <el-button style="margin-bottom: 15px;" icon="Plus" type="primary" size="default" @click="addBtn">新增</el-button>
            <!-- 表格 -->
    <el-table :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        row-key="menuId" :data="tableList" border stripe>
        <el-table-column prop="title" label="菜单名称"></el-table-column>
        <el-table-column prop="type" label="菜单类型">
            <template #default="scope">
                <el-tag v-if="scope.row.type == '1'" type="success" size="default">菜单
                </el-tag>
                <el-tag v-if="scope.row.type == '2'" type="danger" size="default">按钮
                </el-tag>
            </template>
        </el-table-column>
        <el-table-column prop="icon" label="图标">
            <template #default="scope">
                <el-icon>
                    <!-- 动态组件展示图标 -->
                     <component v-if="scope.row.icon" :is="scope.row.icon"></component>
                </el-icon>
            </template>
        </el-table-column>
        <el-table-column prop="parentName" label="上级菜单"></el-table-column>
        <el-table-column prop="path" label="路由"></el-table-column>
        <el-table-column prop="code" label="权限字段"></el-table-column>
        <el-table-column prop="orderNum" label="序号"></el-table-column>
        <el-table-column  label="操作" align="center" width="220">
            <template #default="scope"  >
                <el-button icon="Edit" type="primary" size="default"
                    @click="editBtn(scope.row)">编辑</el-button>
                <el-button icon="Delete" type="danger" size="default"
                    @click="deletBtn(scope.row)">删除</el-button>
            </template>
        </el-table-column>
     </el-table>
    </el-main>
    <!-- 新增弹框 -->
     <SysDialog
     :title="dialog.title"
     :visible="dialog.visible"
     :height="dialog.height"
     :width="dialog.width"
     @onClose="onClose"
     @onConfirm="commit"
     >
        <template v-slot:content>
            <el-form :model="addModel" ref="addRef" :rules="rules" label-width="80px" :inline="false" size="default">
                <el-form-item prop="type" label="菜单类型">
                    <el-radio-group v-model="addModel.type">
                    <el-radio :value="1">菜单</el-radio>
                    <el-radio :value="2">按钮</el-radio>
  <                 </el-radio-group>
                </el-form-item>
                <el-row>
                    <el-col :span="12" :offset="0">
                        <el-form-item prop="parentId"label="上级菜单">
                            <!-- check-strictly仅可选中一个 -->
                            <el-tree-select
                            @check="nodeClick"
                            v-model="addModel.parentId"
                            :data="parentList"
                            :render-after-expand="false"
                            show-checkbox
                            check-strictly
                                />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" :offset="0">
                        <el-form-item prop="title"label="菜单名称">
                            <el-input v-model="addModel.title"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                    <el-row>
                        <el-col :span="12" :offset="0">
                            <el-form-item prop="code" label="权限字段">
                            <el-input v-model="addModel.code"></el-input>
                        </el-form-item>
                        </el-col>
                        <el-col :span="12" :offset="0">
                            <el-form-item prop="orderNum" label="菜单序号">
                            <el-input v-model="addModel.orderNum"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row v-if="addModel.type !='2'">
                        <el-col :span="12" :offset="0">
                            <el-form-item prop="path" label="路由">
                            <el-input v-model="addModel.path"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12" :offset="0">
                            <el-form-item prop="icon" label="图标">
                            <el-input v-model="addModel.icon"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
            </el-form>
        </template>
     </SysDialog>
</template>

<script setup lang="ts">
import { nextTick, onMounted, reactive, ref } from 'vue';
import SysDialog from '../../components/SysDialog.vue';
import useDialog from '../../hooks/useDialog';
import { getParentApi,addApi,listApi,editApi,deleteApi } from '../../api/menu';
import { ElMessage, type FormInstance } from 'element-plus';
import type{ Menu } from '../../api/menu/MenuModel';
import { EditType } from '../../type/BaseEnum';
import useInstance from '../../hooks/useInstance';
//获取全局属性
const{global}=useInstance()
//表单ref属性
const addRef=ref<FormInstance>()
//弹框属性
const{dialog,onClose,onShow}=useDialog()

//弹框绑定属性
const addModel = reactive({
    menuId: "",
    parentId: "",
    title: "",
    code: "",
    type: "",
    icon: "",
    path: "",
    parentName: "",
    orderNum: "",
});
//表单验证规则
const rules = reactive({
type: [
{
required: true,
message: "请选择菜单类型",
trigger: "change",
},
],
parentId: [
{
required: true,
message: "请选择上级菜单",
trigger: "change",
},
],
title: [
{
required: true,
message: "请填写菜单标题",
trigger: "change",
},
],
code: [
{
required: true,
message: "请填写菜单权限字段",
trigger: "change",
},
],
icon: [
{
required: true,
message: "请填写菜单图标",
trigger: "change",
},
],
path: [
{
required: true,
message: "请填写菜单path",
trigger: "change",
},
],
orderNum: [
{
required: true,
message: "请填写序号",
trigger: "change",
},
],

});
//上级菜单树的数据
const parentList = ref([])
//获取上级菜单数据
const getParentList=async()=>{
    let res=await getParentApi()
    if(res && res.code==200){
        parentList.value=res.data;
    }
}
//
const nodeClick=(e:any)=>{
    //console.log(e.title)
    addModel.parentName=e.title;
}
//表格数据
const tableList=ref([])
//获取表格数据
const getList=async()=>{
    let res=await listApi()
    if(res && res.code==200){
        console.log(res)
        tableList.value=res.data;
    }
}
//新增编辑的标识
const tags=ref('')
//新增
const addBtn=()=>{
    tags.value="0";
    dialog.height=220;
    //获取上级菜单
    getParentList();
    //展示弹框
    onShow();
    //清空表单
    addRef.value?.resetFields();
};
//删除
const deletBtn=async(row:Menu)=>{
    console.log(row)
    //信息确定
    const confirm=await global.$myconfirm("确定删除该数据吗？");
    if(confirm){
        let res=await deleteApi(row);
        if(res && res.code==200){
            ElMessage.success(res.msg);
            getList();
        }
    }
}
//编辑
const editBtn=(row:Menu)=>{
    tags.value="1";
    console.log(row)
    dialog.height=220;
    //获取上级菜单
    getParentList();
    //数据回显
    nextTick(()=>{
        Object.assign(addModel,row)//把row数据写给addModel
    })
    //显示弹框
    onShow()
    //清空表单
    addRef.value?.resetFields();
}
//确定
const commit=async()=>{
    addRef.value?.validate(async(valid)=>{
        if(valid){
            let res=null
            if(tags.value==EditType.ADD){
                res=await addApi(addModel);
            }
            else{
                res=await editApi(addModel)
            }
            console.log(res)
            if(res && res.code==200){
                ElMessage.success(res.msg)
                getList()
                onClose();
            }
        }
    })
    
};
onMounted(()=>{
    getList()
})
</script>

<style scoped>

</style>