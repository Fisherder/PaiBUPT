<template>
	<view class="u-wrap u-p-l-20 u-p-r-20">
		<u-form :model="addModel" ref="form1">
			<u-form-item prop="name" label="类型:">
				<u-radio-group v-model="addModel.name">
					<u-radio @change="radioChange" activeColor="#FF7670" v-for="
(item, index) in list" :key="index" :name="item.name" :disabled="item.disabled">
						{{ item.name }}
					</u-radio>
				</u-radio-group>
			</u-form-item>
			<u-form-item prop="goodsName" label="名称:">
				<u-input placeholder="请输入名称" v-model="addModel.goodsName" />
			</u-form-item>
			<u-form-item prop="categoryName" label="分类:">
				<u-input @click="openSelect" placeholder="请选择分类" v￾model="addModel.categoryName" />
				<u-select @confirm="selectConfirm" v-model="show" :list="selectList"></u-select>
			</u-form-item>
			<u-form-item prop="goodsDesc" label="简介:">
				<u-input placeholder="请输入简介" v-model="addModel.goodsDesc" />
			</u-form-item>
			<u-form-item prop="goodsPrice" label="价格:">
				<u-input placeholder="请输入价格" v-model="addModel.goodsPrice" />
			</u-form-item>
			<u-form-item prop="userName" label-width="auto" label="联系人:">
				<u-input placeholder="请输入联系人" v-model="addModel.userName" />
			</u-form-item>
			<u-form-item prop="phone" label-width="auto" label="联系电话:">
				<u-input placeholder="请输入联系电话" v-model="addModel.phone" />
			</u-form-item>
			<u-form-item prop="wxNum" label-width="auto" label="微信号:">
				<u-input placeholder="请输入微信号" v-model="addModel.wxNum" />
			</u-form-item>
			<u-form-item prop="address" label-width="auto" label="联系地址:">
				<view class="address-input-container">
					<u-input v-model="addModel.address" placeholder="请输入或选择地址" />
					<u-button @click="chooseLocation" size="mini" type="primary"
						class="choose-location-btn">选择位置</u-button>
				</view>
			</u-form-item>
			<u-form-item prop="image" label="图片:"></u-form-item>
			<u-upload ref="imgRef" :file-list="fileList" @on-remove="onRemove" @on-change="onChange"
				:action="action"></u-upload>
		</u-form>
		<u-button @click="commit" :custom-style="customStyle">确定编辑</u-button>
	</view>
</template>
<script setup>
	import {
		reactive,
		ref
	} from 'vue';
	import {
		onLoad,
		onReady
	} from '@dcloudio/uni-app'
	import http from '../../common/http.js'
	// 在script部分添加
	import mapService from '../../utils/map-service';
	import {
		categoryApi,
		editApi
	} from '../../api/goods.js'
	//表单ref属性
	const form1 = ref()
	const imgRef = ref()
	const addModel = reactive({
		userId: uni.getStorageSync('userId'),
		name: '',
		type: '',
		goodsName: '',
		categoryId: '',
		categoryName: '',
		goodsDesc: '',
		goodsPrice: '',
		userName: '',
		phone: '',
		wxNum: '',
		image: '',
		address: ''
	})
	const list = [{
			value: '0',
			name: '闲置',
			disabled: false
		},
		{
			value: '1',
			name: '求购',
			disabled: false
		}
	]
	const rules = reactive({
		name: [{
			required: true,
			message: '请选择类型',
			trigger: ['change', 'blur'],
		}],
		goodsName: [{
			required: true,
			message: '请填写名称',
			trigger: ['change', 'blur'],
		}],
		goodsDesc: [{
			required: true,
			message: '请填写描述',
			trigger: ['change', 'blur'],
		}],
		goodsPrice: [{
			required: true,
			message: '请填写价格',
			trigger: ['change', 'blur'],
			transform(value) {
				return String(value)
			}
		}],
		userName: [{
			required: true,
			message: '请填写姓名',
			trigger: ['change', 'blur'],
		}],
		phone: [{
			required: true,
			message: '请填写电话',
			trigger: ['change', 'blur'],
		}],
		wxNum: [{
			required: true,
			message: '请填写微信号',
			trigger: ['change', 'blur'],
		}],
		address: [{
			required: true,
			message: '请填写发布/求购地址',
			trigger: ['change', 'blur'],
		}]
	})
	const value = ref('')
	const fileList = ref([])
	const customStyle = reactive({
		background: '#FF7670',
		color: '#FFF',
		marginTop: '50px',
		width: '100%'
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
		addModel.image = url.substring(0, url.lastIndexOf(','))
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
		addModel.image = url.substring(0, url.lastIndexOf(','))
		console.log(addModel)
	}
	//分类
	const show = ref(false)
	//分类数据
	const cageId = ref('')
	const selectList = ref([])
	const getSelectList = async () => {
		let res = await categoryApi()
		if (res && res.code == 200) {
			console.log(res)
			selectList.value = res.data;
			//设置分类
			if (cageId.value) {
				for (let k = 0; k < selectList.value.length; k++) {
					if (selectList.value[k].value == cageId.value) {
						addModel.categoryName = selectList.value[k].label;
					}
				}
			}
		}
	}
	//打开分类
	const openSelect = () => {
		show.value = true;
	}
	//分类确定
	const selectConfirm = (e) => {
		console.log(e)
		addModel.categoryName = e[0].label;
		addModel.categoryId = e[0].value;
	}
	//发布提交
	const commit = () => {
		form1.value.validate(async (valid) => {
			console.log(addModel)
			if (valid) {
				let res = await editApi(addModel)
				if (res && res.code == 200) {
					console.log(res)
					uni.navigateTo({
						url: '../my_unused/my_unused'
					})
					//清空数据
					form1.value.resetFields()
					imgUrl.value = []
					addModel.image = '';
					fileList.value = []
					imgRef.value.clear()
				}
			}
		})
		console.log(addModel)
	}
	// 选择位置方法
	const chooseLocation = () => {
		uni.chooseLocation({
			success: function(res) {
				console.log('选择位置成功:', res);
				addModel.address = res.address;
	
				// 可以保存经纬度以提高位置精确性
				if (!addModel.locationInfo) {
					addModel.locationInfo = {};
				}
				addModel.locationInfo.latitude = res.latitude;
				addModel.locationInfo.longitude = res.longitude;
				addModel.locationInfo.name = res.name;
			},
			fail: function(err) {
				console.error('选择位置失败:', err);
				if (err.errMsg.indexOf('auth deny') >= 0) {
					uni.showModal({
						title: '提示',
						content: '需要授权获取位置信息才能选择位置',
						confirmText: '去设置',
						success: (res) => {
							if (res.confirm) {
								uni.openSetting({
									success: (settingRes) => {
										console.log('设置结果:', settingRes);
									}
								});
							}
						}
					});
				}
			}
		});
	};
	//类型点击事件
	const radioChange = (e) => {
		console.log(e)
		for (let i = 0; i < list.length; i++) {
			if (list[i].name == e) {
				addModel.type = list[i].value;
			}
		}
	}
	onReady(() => {
		//设置表单验证规则
		form1.value.setRules(rules);
		//设置图片回显
		imgRef.value.lists = fileList.value
		//获取分类数据
		getSelectList()
	})
	onLoad((options) => {
		imgUrl.value = [];
		fileList.value = [];
		const goods = JSON.parse(options.goods)
		cageId.value = goods.categoryId;
		console.log(goods)
		addModel.goodsId = goods.goodsId;
		addModel.categoryId = goods.categoryId;
		addModel.image = goods.image;
		//物品的图片回显
		if (goods.image) {
			let imgs = goods.image.split(",");
			for (let g = 0; g < imgs.length; g++) {
				imgUrl.value.push(imgs[g]);
				let obj = {
					url: ''
				}
				obj.url = imgs[g]
				fileList.value.push(obj)
			}
		}
		addModel.goodsName = goods.goodsName;
		addModel.goodsDesc = goods.goodsDesc;
		addModel.address = goods.address;
		addModel.goodsPrice = goods.goodsPrice
		addModel.userName = goods.userName;
		addModel.phone = goods.phone
		addModel.type = goods.type
		addModel.wxNum = goods.wxNum
		//设置类型
		for (let i = 0; i < list.length; i++) {
			if (list[i].value == goods.type) {
				addModel.name = list[i].name;
			}
		}
	})
</script>
<style>
</style>