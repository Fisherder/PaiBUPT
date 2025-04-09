<template>
	<view class="u-wrap u-p-l-20 u-p-r-20">
		<u-form label-width="auto" :model="addModel" ref="form1">
			<u-form-item label="原密码" prop="oldPassword">
				<u-input v-model="addModel.oldPassword" />
			</u-form-item>
			<u-form-item label="新密码" prop="password">
				<u-input v-model="addModel.password" />
			</u-form-item>
			<u-form-item label="确定密码" prop="confirm">
				<u-input v-model="addModel.confirm" />
			</u-form-item>
		</u-form>
		<u-button @click='commit' :custom-style="customStyle">确定修改</u-button>
	</view>
</template>

<script setup>
	import {
		onReady
	} from '@dcloudio/uni-app';
	import {
		reactive,
		ref
	} from 'vue'
	import {
		wxupdatePasswordApi
	} from '../../api/user.js'
	const form1 = ref('')
	const addModel = reactive({
		userId: uni.getStorageSync("userId"),
		password: '',
		oldPassword: '',
		confirm: ''
	})
	const customStyle = reactive({
		background: '#FF7670',
		color: '#FFF',
		marginTop: '50px',
		width: '100%'
	})
	const rules = reactive({
		oldPassword: [{
			required: true,
			message: '请输入原密码',
			trigger: ['change'],
		}],
		password: [{
			required: true,
			message: '请输入新密码',
			trigger: ['change'],
		}],
		confirm: [{
			required: true,
			message: '请输入确定密码',
			trigger: ['change'],
		}]
	})
	//表单提交
	const commit = () => {
		form1.value.validate(async (valid) => {
			if (addModel.confirm != addModel.password) {
				uni.showToast({
					title: '新密码和确定密码不一致!',
					icon: 'none',
					mask: true,
					duration: 3000
				})
				return;
			}
			if (valid) {
				console.log('验证通过')
				let res = await wxupdatePasswordApi(addModel)
				if (res && res.code == 200) {
					//清空缓存
					uni.clearStorageSync();
					//重新登录：关闭所有的页面
					uni.reLaunch({
						url: '../login/login'
					});
				}
			}
		})
	}
	onReady(() => {
		form1.value.setRules(rules);
	})
</script>

<style>

</style>