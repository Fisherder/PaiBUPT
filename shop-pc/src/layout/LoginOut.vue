<template>
    <el-dropdown>
        <span class="el-dropdown-link">
            <img class="userimg" src="@/assets/user.jpg">
        </span>
        <template #dropdown>
            <el-dropdown-menu>
                <el-dropdown-item @click="upBtn">修改密码</el-dropdown-item>
                <el-dropdown-item @click="loginBtn">退出登录</el-dropdown-item>
            </el-dropdown-menu>
        </template>
    </el-dropdown>
    <!-- 修改密码弹框 -->
    <SysDialog :title="dialog.title" :width="dialog.width" :height="dialog.height" :visible="dialog.visible"
        @onClose="onClose" @onConfirm="commit">
        <template v-slot:content>
            <el-form :model="addModel" ref="form" :rules="rules" label-width="80px" :inline="false" size="default">
                <el-form-item prop="oldPassword" label="原密码">
                    <el-input v-model="addModel.oldPassword"></el-input>
                </el-form-item>
                <el-form-item prop="password" label="新密码">
                    <el-input v-model="addModel.password"></el-input>
                </el-form-item>
                <el-form-item prop="newPassword" label="确定密码">
                    <el-input v-model="addModel.newPassword"></el-input>
                </el-form-item>
            </el-form>
        </template>
    </SysDialog>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import useInstance from '../hooks/useInstance';
import SysDialog from '@/components/SysDialog.vue';
import useDialog from '../hooks/useDialog';
import { reactive, ref } from 'vue';
import { ElMessage, type FormInstance } from 'element-plus';
import { updatePasswordApi } from '../api/user';
import { userStore } from '../store/user';
const store = userStore()
const { dialog, onClose,  onShow } = useDialog();
//表单ref属性
const form = ref<FormInstance>()
const router = useRouter();
//表单绑定的对象
const addModel = reactive({
    userId: "",
    password: "",
    oldPassword: "",
    newPassword: "",
});
//表单验证规则
const rules = reactive({
    oldPassword: [
        {
            required: "true",
            trigger: "change",
            message: "请输入原密码",
        },
    ],
    password: [
        {
            required: "true",
            trigger: "change",
            message: "请输入新密码",
        },
    ],
    newPassword: [
        {
            required: "true",
            trigger: "change",
            message: "请输入确定密码",
        },
    ],
});
//获取全局属性
const { global } = useInstance();
//修改密码
const upBtn = () => {
    addModel.userId = store.getUserId;
    dialog.title = '修改密码';
    dialog.height = 150;
    onShow()
}
//退出登录按钮
const loginBtn = async () => {
    const confirm = await global.$myconfirm('确认退出登录吗？')
    if (confirm) {
        //清空缓存
        sessionStorage.clear()
        //跳转登录
        router.push({ name: 'login' })
    }
}
//表单提交
const commit = () => {
    //新密码和确定密码判断
    if (addModel.newPassword != addModel.password) {
        ElMessage.warning("新密码和确定密码不一致!");
        return;
    }
    form.value?.validate(async (valid) => {
        if (valid) {
            let res = await updatePasswordApi(addModel);
            if (res && res.code == 200) {
                ElMessage.success(res.msg);
                //清空缓存，重新登录
                sessionStorage.clear();
                //跳转登录
                router.push({ name: "login" });
            }
        }
    });
};
</script>

<style scoped>
.userimg {
    height: 55px;
    width: 55px;
    border-radius: 50%;
}

.el-dropdown-link:focus {
    outline: none;
}
</style>