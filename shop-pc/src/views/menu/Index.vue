<template>
    <!-- 菜单管理，不同管理员可调用的菜单不同 -->
    <el-main>
        <el-button icon="Plus" type="primary" size="default" @click="addBtn">新增</el-button>
    </el-main>
    <!-- 新增弹框 -->
     <SysDialog
     :title="dialog.title"
     :visible="dialog.visible"
     :height="dialog.height"
     :width="dialog.width"
     @onClose="onClose"
     @onConfirm="onConfirm"
     >
        <template v-slot:content>
            <el-form :model="addModel" ref="addRef" :rules="rules" label-width="80px" :inline="false" size="default">
                <el-form-item label="菜单类型">
                    <el-radio-group v-model="addModel.type">
                    <el-radio :value="1">菜单</el-radio>
                    <el-radio :value="2">按钮</el-radio>
  <                 </el-radio-group>
                </el-form-item>
                <el-row>
                    <el-col :span="12" :offset="0">
                        <el-form-item label="上级菜单">
                            <el-tree-select
                            v-model="addModel.parentId"
                            :data="parentList"
                            :render-after-expand="false"
                            show-checkbox
                                />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" :offset="0">
                        <el-form-item label="菜单名称">
                            <el-input v-model="addModel.title"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                    <el-row>
                        <el-col :span="12" :offset="0">
                            <el-form-item label="权限字段">
                            <el-input v-model="addModel.code"></el-input>
                        </el-form-item>
                        </el-col>
                        <el-col :span="12" :offset="0">
                            <el-form-item label="菜单序号">
                            <el-input v-model="addModel.orderNum"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row v-if="addModel.type !='2'">
                        <el-col :span="12" :offset="0">
                            <el-form-item label="路由">
                            <el-input v-model="addModel.path"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12" :offset="0">
                            <el-form-item label="图标">
                            <el-input v-model="addModel.icon"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
            </el-form>
        </template>
     </SysDialog>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import SysDialog from '../../components/SysDialog.vue';
import useDialog from '../../hooks/useDialog';
import { getParentApi } from '../../api/menu';
//弹框属性
const{dialog,onClose,onConfirm,onShow}=useDialog()
const addBtn=()=>{
    dialog.height=220;
    //获取上级菜单
    getParentList();
    //展示弹框
    onShow();

};
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
const rules = reactive({});
//上级菜单树的数据
const parentList = ref([])
//获取上级菜单数据
const getParentList=async()=>{
    let res=await getParentApi()
    if(res && res.code==200){
        parentList.value=res.data;
    }
}
onMounted(()=>{
    
})
</script>

<style scoped>

</style>