<template>
	<view class="page-box">
		<view class="order" v-for="(item, index) in list" :key="item.goodsId">
			<view class="top">
				<view class="left">
					<!-- <u-icon name="home" :size="30" color="rgb(94,94,94)">
</u-icon> -->
					<view class="store">{{ item.goodsName }}</view>
				</view>
				<view style="color: #82848a;" v-if="item.status == '0'" class="right">已上架</view>
				<view style="color: #FF7670;" v-if="item.status == '1'" class="right">已下架</view>
			</view>
			<view class="item">
				<view class="left">
					<image :src="item.image.split(',')[0]" mode="aspectFill">
					</image>
				</view>
				<view class="content">
					<view class="title u-line-2">{{ item.goodsDesc }}</view>
					<view class="type">{{ item.address }}</view>
					<view class="delivery-time">发布时间 {{ item.createTime }}
					</view>
				</view>
				<view class="right">
					<view style="color: #FF7670;" class="price">
						￥{{ item.goodsPrice }}
					</view>
					<view class="number">x1</view>
				</view>
			</view>
			<view class="bottom">
				<view @click="lookBtn(item)" class="logistics btn">查看</view>
				<view @click="upanddownBtn(item)" class="exchange btn">
					<spna v-if="item.status == '0'">下架</spna>
					<spna v-if="item.status == '1'">上架</spna>
				</view>
				<view @click="editBtn(item)" class="evaluate btn">编辑</view>
				<view @click="deleteBtn(item)" class="evaluate btn">删除</view>
			</view>
		</view>
		<u-loadmore :status="loadstatus" @loadmore="loadMore" bgColor="#f2f2f2">
		</u-loadmore>
		<u-popup :mask-close-able="false" border-radius="15" width="70%" height="120px" v-model="show" mode="center">
			<view style="padding: 20px 15px 20px 15px;">
				确定删除该数据吗?
			</view>
			<view class="conBtn">
				<u-button @click="cancel" style="margin-right: 15px;" type="info">取消</u-button>
				<u-button @click="confirm" style="margin-left: 15px;" :custom-style="customStyle"
					type="error">确定</u-button>
			</view>
		</u-popup>
	</view>
</template>
<script setup>
	import {
		onReachBottom,
		onReady
	} from '@dcloudio/uni-app';
	import {
		reactive,
		ref
	} from 'vue';
	import {
		getMyUnusedListApi,
		upanddownApi,
		deleteApi
	} from '../../api/goods.js'
	const list = ref([])
	const customStyle = reactive({
		background: '#FF7670'
	})
	//列表查询的参数
	const parm = reactive({
		userId: uni.getStorageSync("userId"),
		type: '1',
		currentPage: 1,
		pageSize: 10
	})
	//获取列表数据
	//总页数
	const pages = ref(0)
	const getMyUnusedList = async () => {
		let res = await getMyUnusedListApi(parm)
		if (res && res.code == 200) {
			console.log(res)
			pages.value = res.data.pages;
			list.value = list.value.concat(res.data.records)
		}
	}
	const loadstatus = ref('loadmore')
	//加载更多
	const loadMore = () => {
		console.log('点击加载更多了')
		if (parm.currentPage >= pages.value) {
			loadstatus.value = "nomore"
			return;
		}
		loadstatus.value = "loading"
		parm.currentPage = ++parm.currentPage;
		getMyUnusedList()
	}
	onReachBottom(() => {
		console.log('触底加载更多了')
		if (parm.currentPage >= pages.value) {
			loadstatus.value = "nomore"
			return;
		}
		loadstatus.value = "loading"
		parm.currentPage = ++parm.currentPage;
		getMyUnusedList()
	})
	//编辑
	const editBtn = (item) => {
		uni.navigateTo({
			url: "../buy_edit/buy_edit?goods=" + JSON.stringify(item)
		})
	}
	//查看
	const lookBtn = (item) => {
		uni.navigateTo({
			url: "../look_buy/look_buy?goods=" + JSON.stringify(item)
		})
	}
	//下架
	const upanddownBtn = async (item) => {
		console.log(item)
		let res = await upanddownApi({
			goodsId: item.goodsId,
			status: item.status == '1' ? '0' : '1'
		})
		if (res && res.code == 200) {
			list.value = []
			parm.currentPage = 1;
			getMyUnusedList()
		}
	}
	//删除
	const deleteId = ref('')
	const show = ref(false)
	//删除按钮点击事件
	const deleteBtn = (item) => {
		console.log(item)
		deleteId.value = item.goodsId;
		show.value = true;
		console.log(deleteId.value)
	}
	const cancel = () => {
		show.value = false;
	}
	const confirm = async () => {
		let res = await deleteApi({
			goodsId: deleteId.value
		})
		if (res && res.code == 200) {
			list.value = []
			parm.currentPage = 1;
			getMyUnusedList()
			show.value = false;
		}
	}
	onReady(() => {
		getMyUnusedList()
	})
</script>
<style lang="scss">
	.conBtn {
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.order {
		width: 710rpx;
		background-color: #ffffff;
		margin: 20rpx auto;
		border-radius: 20rpx;
		box-sizing: border-box;
		padding: 20rpx;
		font-size: 28rpx;

		.top {
			display: flex;
			justify-content: space-between;

			.left {
				display: flex;
				align-items: center;

				.store {
					margin: 0 10rpx;
					font-size: 32rpx;
					font-weight: bold;
				}
			}

			.right {
				color: $u-type-warning-dark;
			}
		}

		.item {
			display: flex;
			margin: 20rpx 0 0;

			.left {
				margin-right: 20rpx;

				image {
					width: 200rpx;
					height: 200rpx;
					border-radius: 10rpx;
				}
			}

			.content {
				.title {
					font-size: 28rpx;
					line-height: 50rpx;
				}

				.type {
					margin: 10rpx 0;
					font-size: 24rpx;
					color: $u-tips-color;
				}

				.delivery-time {
					color: #e5d001;
					font-size: 24rpx;
				}
			}

			.right {
				margin-left: 10rpx;
				padding-top: 20rpx;
				text-align: right;

				.decimal {
					font-size: 24rpx;
					margin-top: 4rpx;
				}

				.number {
					color: $u-tips-color;
					font-size: 24rpx;
				}
			}
		}

		.total {
			margin-top: 20rpx;
			text-align: right;
			font-size: 24rpx;

			.total-price {
				font-size: 32rpx;
			}
		}

		.bottom {
			display: flex;
			margin-top: 40rpx;
			padding: 0 10rpx;
			justify-content: space-between;
			align-items: center;

			.btn {
				line-height: 52rpx;
				width: 160rpx;
				border-radius: 26rpx;
				border: 2rpx solid $u-border-color;
				font-size: 26rpx;
				text-align: center;
				color: $u-type-info-dark;
				margin-right: 10px;
			}

			.evaluate {
				color: $u-type-warning-dark;
				border-color: $u-type-warning-dark;
			}
		}
	}
</style>