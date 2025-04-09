<template>
	<view class="u-wrap u-p-l-20 u-p-r-20">
		<u-form label-width="auto" :model="addModel" ref="form1">
			<u-form-item label="登录账户" prop="username">
				<u-input v-model="addModel.username" />
			</u-form-item>
			<u-form-item label="电话号码" prop="phone">
				<u-input v-model="addModel.phone" />
			</u-form-item>
			<u-form-item label="新密码" prop="password">
				<u-input type="password" v-model="addModel.password" />
			</u-form-item>
			<u-form-item label="确定密码" prop="confirm">
				<u-input type="password "v-model="addModel.confirm" />
			</u-form-item>
			<u-form-item label="验证码" prop="code">
				<u-input v-model="addModel.code" />
				<template v-slot:right>
					<view class="code-img-wrapper" @click="updateImageCode">
						<canvas style="width:220rpx;height:86rpx;" canvas-id="canvas"></canvas>
					</view>
				</template>
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
		Mcaptcha
	} from '../../common/mcaptcha.js'
	import {
		forgetApi
	} from '../../api/user.js';
	import {
		ref,
		reactive
	} from 'vue';
	const form1 = ref('')
	const addModel = reactive({
		password: '',
		username: '',
		confirm: '',
		phone: '',
		code: ''
	})
	//验证码定义
	let mcaptcha = null;
	const customStyle = reactive({
		background: '#FF7670',
		color: '#FFF',
		marginTop: '50px',
		width: '100%'
	})
	const rules = reactive({
		username: [{
			required: true,
			message: '请输入登录账户',
			trigger: ['change'],
		}],
		phone: [{
			required: true,
			message: '请输入电话号码',
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
			},
			{
				validator: (rule, value, callback) => {
					let validate = (value == addModel.password)
					return validate;
				},
				message: '新密码和确定密码不一致！！'
			}
		],
		code: [{
				required: true,
				message: '请输入验证码',
				trigger: ['change'],
			},
			{
				validator: (rule, value, callback) => {
					let validate = mcaptcha.validate(addModel.code)
					return validate;
				},
				message: '验证码不正确！'
			}
		],
	})
	//表单提交
	const commit = () => {
		form1.value.validate(async (valid) => {
			console.log('验证通过')
			if (valid) {
				let res = await forgetApi(addModel)
				if (res && res.code == 200) {
					uni.navigateBack({
						delta: 1
					})
				}
			}
		})
	}
	//点击验证码事件
	const updateImageCode = () => {
		mcaptcha.refresh()
	}
	onReady(() => {
		//初始化验证码
		mcaptcha = new Mcaptcha({
			el: 'canvas',
			width: 80,
			height: 35,
			createCodeImg: ""
		});
		form1.value.setRules(rules);
	})
</script>
<style>
</style>