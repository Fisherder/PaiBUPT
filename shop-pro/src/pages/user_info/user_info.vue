<template>
	<view class="u-wrap u-p-l-20 u-p-r-20">
		<u-form label-width="auto" :model="addModel" ref="form1">
			<u-form-item label="昵称" prop="nickName">
				<u-input v-model="addModel.nickName" />
			</u-form-item>
			<u-form-item label="姓名" prop="name">
				<u-input v-model="addModel.name" />
			</u-form-item>
			<u-form-item label="电话" prop="phone">
				<u-input v-model="addModel.phone" />
			</u-form-item>
			<u-form-item prop="picture" label="图片:"></u-form-item>
			<u-upload max-count="1" ref="imgRef" :file-list="fileList" @on-remove="onRemove" @on-change="onChange"
				:action="action"></u-upload>
		</u-form>
		<u-button @click='commit' :custom-style="customStyle">确定修改</u-button>
	</view>
</template>
<script setup>
	import {
		onLoad,
		onReady
	} from '@dcloudio/uni-app';
	import {
		ref,
		reactive
	} from 'vue';
	import http from '../../common/http.js'
	const imgRef = ref()
	import {
		editInfoApi
	} from '../../api/user.js'
	const form1 = ref('')
	const fileList = ref([])
	const addModel = reactive({
		userId: uni.getStorageSync("userId"),
		nickName: '',
		name: '',
		phone: '',
		picture: ''
	})
	const customStyle = reactive({
		background: '#FF7670',
		color: '#FFF',
		marginTop: '50px',
		width: '100%'
	})
	const rules = reactive({
		nickName: [{
			required: true,
			message: '请输入昵称',
			trigger: ['change'],
		}],
		name: [{
			required: true,
			message: '请输入姓名',
			trigger: ['change'],
		}],
		phone: [{
			required: true,
			message: '请输入电话',
			trigger: ['change'],
		}],
		picture: [{
			required: true,
			message: '请上传头像',
			trigger: ['change'],
		}]
	})
	//图片上传路径
	const action = ref(http.baseUrl + "/api/upload/uploadImage")
	//存储图片路径
	const imgUrl = ref([])
	//图片上传触发
	const onChange = (res, index, lists, name) => {
		// console.log(res.data)
		let result = JSON.parse(res.data)
		//把返回的图片放到imgUrl
		imgUrl.value.push(http.baseUrl + result.data)
		//把数组里面的图片转为逗号分隔的字符串
		let url = ''
		for (let k = 0; k < imgUrl.value.length; k++) {
			url = url + imgUrl.value[k] + ','
		}
		// console.log(url)
		//去掉末尾的逗号
		addModel.picture = url.substring(0, url.lastIndexOf(','))
		console.log(addModel)
	}
	//删除图片
	const onRemove = (index) => {
		//删除图片
		imgUrl.value.splice(index, 1)
		let url = ''
		for (let k = 0; k < imgUrl.value.length; k++) {
			url = url + imgUrl.value[k] + ','
		}
		// console.log(url)
		//去掉末尾的逗号
		addModel.picture = url.substring(0, url.lastIndexOf(','))
		console.log(addModel)
	}
	//表单提交
	const commit = () => {
		form1.value.validate(async (valid) => {
			if (valid) {
				console.log('验证通过')
				let res = await editInfoApi(addModel)
				if (res && res.code == 200) {
					//关闭当前页面
					uni.navigateBack({
						delta: 1
					})
				}
			}
		})
	}
	onReady(() => {
		form1.value.setRules(rules);
		//设置图片回显
		imgRef.value.lists = fileList.value
	})
	onLoad((options) => {
		console.log(options)
		const userInfo = JSON.parse(options.userInfo)
		addModel.name = userInfo.name;
		addModel.nickName = userInfo.nickName
		addModel.phone = userInfo.phone
		addModel.picture = userInfo.picture
		//头像回显
		if (userInfo.picture) {
			let imgs = userInfo.picture.split(",");
			for (let g = 0; g < imgs.length; g++) {
				imgUrl.value.push(imgs[g]);
				let obj = {
					url: ''
				}
				obj.url = imgs[g]
				fileList.value.push(obj)
			}
		}
	})
</script>
<style>
</style>