<template>
    <div>
        <el-button type="primary" size="default" @click="addBtn">新增</el-button>
        <SysDialog 
        :title="dialog.title"
        :visible="dialog.visible"
        append-to-body
        :close-on-click-modal="false"
        @on-close="onClose"
        @on-confirm="onConfirm"
        >
        <template v-slot:content>弹框内容

        </template>
        </SysDialog>
    </div>
</template>

<script setup lang="ts">
import SysDialog from '@/components/SysDialog.vue';
import useDialog from '../hooks/useDialog';
//获取弹框属性
const{dialog,onClose,onConfirm,onShow} = useDialog()
// //定义弹框属性
// const dialog=reactive({
//     title:'新增',
//     visible:false,
// })
// //按钮点击事件
 const addBtn = ()=>{
     onShow()
 }
// //弹框关闭
// const onClose=()=>{
//     dialog.visible=false;
// }
// const onConfirm=()=>{
//     //提交请求，成功之后才关闭
//     dialog.visible=false;
// }
</script>

<style scoped>

</style>