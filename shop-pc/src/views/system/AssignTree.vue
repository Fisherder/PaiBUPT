<template>
    <SysDialog :title="dialog.title" :width="dialog.width" :height="dialog.height" :visible="dialog.visible"
        @onClose="onClose" @onConfirm="commit">
        <template v-slot:content>
            <el-tree ref="assignTree" :data="assignTreeData.list" node-key="menuId" :props="defaultProps"
                empty-text="暂无数据" :show-checkbox="true" :highlight-current="true" default-expand-all
                :default-checked-keys="assignTreeData.assignTreeChecked"></el-tree>
        </template>
    </SysDialog>
</template>

<script setup lang="ts">
import { type User } from '../../api/user/UserModel';
import SysDialog from '../../components/SysDialog.vue';
import useDialog from '../../hooks/useDialog';
import { assignSaveApi, getAssignTreeApi } from '../../api/user';
import { reactive, ref } from 'vue';
import { userStore } from '../../store/user';
import { ElMessage, type ElTree } from 'element-plus';

//树的ref属性
const assignTree = ref<InstanceType<typeof ElTree>>()
const defaultProps = {
    children: 'children',
    label: 'label',
}
const store = userStore()
//获取弹框属性
const { dialog, onClose, onShow } = useDialog()
//查询树的参数
const parms = reactive({
    userId: "", //当前登录用户的id
    assId: "",
});
//提交参数
const commitParm = reactive({
    assId: '',
    list: [] as string[]
})
//定义树的数据
const assignTreeData = reactive({
    list: [],
    assignTreeChecked: []
});
//获取树的数据
const getAssignTree = async () => {
    let res = await getAssignTreeApi(parms)
    if (res && res.code == 200) {
        assignTreeData.list = res.data.menuList;
        //设置角色原来的权限Id
        assignTreeData.assignTreeChecked = res.data.checkList
        //数据回显，判断角色原来是否已经分配过权限，如果有，回显，且排除半显节点
        if (assignTreeData.assignTreeChecked.length > 0) {
            let newArr: any = [];
            assignTreeData.assignTreeChecked.forEach((item => {
                checked(item, assignTreeData.list, newArr)
            }))
            assignTreeData.assignTreeChecked = newArr;
        }
    }
}
//排除半显节点
const checked = (id: number, data: any, newArr: any) => {
    data.forEach((item: any) => {
        if (item.menuId == id) {
            if (item.children && item.children.length == 0) {
                newArr.push(item.menuId)
            }
        } else {
            if (item.children && item.children.length != 0) {
                //递归调用
                checked(id, item.children, newArr)
            }
        }
    })
}
//提交方法
const commit = async () => {
    // 正确获取选中数据
    console.log(assignTree)
    //选择的节点id
    let checkedIds = assignTree.value?.getCheckedKeys(false) as string[]
    console.log(checkedIds)
    //半选节点id
    let halfIds = assignTree.value?.getHalfCheckedKeys() as string[]
    console.log(halfIds)
    let ids = checkedIds.concat(halfIds) as string[]
    console.log(ids)
    if (ids.length == 0) {
        ElMessage.warning('请选择菜单！')
        return;
    }
    commitParm.list = ids;
    let res = await assignSaveApi(commitParm)
    if (res && res.code == 200) {
        ElMessage.success(res.msg)
        onClose()
    }

}
//弹框显示的方法
const show = (row: User) => {
    commitParm.assId = '';
    commitParm.list = [];
    //要分配的用户id
    parms.assId = row.userId;
    commitParm.assId = row.userId;
    parms.userId = store.getUserId;
    //console.log(parms)
    getAssignTree()
    //console.log(row)
    dialog.title = "为【" + row.nickName + "】分配菜单";
    dialog.width = 300;
    dialog.height = 450;
    onShow();
}
//暴露出去，给外部组件调用
defineExpose({
    show
})
</script>

<style scoped></style>