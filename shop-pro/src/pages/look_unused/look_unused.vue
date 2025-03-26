<template>
	<view class="u-wrap u-p-l-20 u-p-r-20">
		<u-form :model="addModel" ref="form1">
			<u-form-item prop="name" label="类型:">
				<u-radio-group v-model="addModel.name">
					<u-radio :disabled="true" @change="radioChange" activeColor="#FF7670" v-for="(item, index) in list"
						:key="index" :name="item.name">
						{{ item.name }}
					</u-radio>
				</u-radio-group>
			</u-form-item>
			<u-form-item prop="goodsName" label="名称:">
				<u-input disabled placeholder="请输入名称" v-model="addModel.goodsName" />
			</u-form-item>
			<u-form-item prop="categoryName" label="分类:">
				<u-input disabled placeholder="请选择分类" v-model="addModel.categoryName" />
			</u-form-item>
			<u-form-item prop="goodsDesc" label="简介:">
				<u-input disabled placeholder="请输入简介" v-model="addModel.goodsDesc" />
			</u-form-item>
			<u-form-item prop="goodsPrice" label="价格:">
				<u-input disabled placeholder="请输入价格" v-model="addModel.goodsPrice" />
			</u-form-item>
			<u-form-item prop="userName" label-width="auto" label="联系人:">
				<u-input disabled placeholder="请输入联系人" v-model="addModel.userName" />
			</u-form-item>
			<u-form-item prop="phone" label-width="auto" label="联系电话:">
				<u-input disabled placeholder="请输入联系电话" v-model="addModel.phone" />
			</u-form-item>
			<u-form-item prop="wxNum" label-width="auto" label="微信号:">
				<u-input disabled placeholder="请输入微信号" v-model="addModel.wxNum" />
			</u-form-item>
			<u-form-item prop="address" label-width="auto" label="联系地址:">
				<u-input disabled v-model="addModel.address" />
			</u-form-item>
			<u-form-item prop="image" label="图片:"></u-form-item>
			<u-upload ref="imgRef" :file-list="fileList" :deletable="false">
			</u-upload>
		</u-form>
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