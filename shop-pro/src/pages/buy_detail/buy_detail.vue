<template>
	<view style="overflow-y:auto;">
		<u-swiper name='image' border-radius='1' :duration='duration' :interval='interval' :height="height"
			:list="swipperList">
		</u-swiper>
		<view class="header">
			<view class="title">
				{{goodsName}}
			</view>
			<view class="price">
				￥{{goodsPrice}}
			</view>
		</view>
		<view class="info-fa">
			<view class="fa-left">

			</view>
			<view class="goodsInfo">
				物品简介
			</view>
		</view>
		<view class="info">
			<view class="title-desc">
				{{goodsDesc}}
			</view>
		</view>
		<view class="info-fa">
			<view class="fa-left">

			</view>
			<view class="goodsInfo">
				物品位置
			</view>
		</view>
		<view class="info">
			<view class="title-desc">
				{{address}}
			</view>
		</view>
		<view class="info-fa">
			<view class="fa-left">

			</view>
			<view class="goodsInfo">
				交易流程
			</view>
		</view>
		<view class="info">
			<u-steps active-color="#FF7670" style="width: 100%;" mode="number" :list="numlist" :current="4"></u-steps>
		</view>
		<view class="info-fa">
			<view class="fa-left">

			</view>
			<view class="goodsInfo">
				交易方式
			</view>
		</view>
		<view class="info">
			<view class="title-desc">
				自行协商.自提|送货上门|约定交易点|当面验货交易
			</view>
		</view>
		<view class="info-fa">
			<view class="fa-left"></view>
			<view class="goodsInfo">
				发布时间
			</view>
		</view>
		<view class="info">
			<view class="title-desc">
				{{createTime}}
			</view>
		</view>
		<view class="info-fa">
			<view class="fa-left"></view>
			<view class="goodsInfo">
				联系方式
			</view>
		</view>
		<view class="useinfo">
			<view class="title-desc">
				电话:{{phone}}
			</view>
			<view class="title-desc">
				微信:{{wxNum}}
			</view>
		</view>
		<view class="navigation">
			<view class="left">
				<view @click="toHome" class="item">
					<u-icon name="home" :size="40"></u-icon>
					<view class="text u-line-1">首页</view>
				</view>
				<view class="item">
					<u-icon :size="40" name="star"></u-icon>
					<view class="text u-line-1">收藏</view>
				</view>
				<view class="item car">
					<u-icon name="info-circle" :size="40"></u-icon>
					<view class="text u-line-1">举报</view>
				</view>
			</view>
			<view class="right">
				<view @click="callPhone" class="cart btn u-line-1">电话咨询</view>
			</view>
		</view>
	</view>
</template>

<script setup>
	import { onLoad } from '@dcloudio/uni-app';
import {
		ref
	} from 'vue';
	//轮播图高度
	const height = ref('350')
	//是否显示面板指示点
	const indicatorDots = ref(true)
	//是否自动播放
	const autoplay = ref(true)
	//自动切换时间间隔
	const interval = ref(2000)
	//滑动动画时长
	const duration = ref(500)
	const swipperList = ref([])
	const numlist = ref([{
		name: '发布信息'
	}, {
		name: '电话/微信沟通'
	}, {
		name: '当面交易'
	}, {
		name: '交易完成'
	}])
	//名称
	const goodsName = ref('')
	//价格
	const goodsPrice = ref('')
	//简介
	const goodsDesc = ref('')
	//位置
	const address = ref('')
	const wxNum = ref('')
	const phone = ref('')
	//发布时间
	const createTime = ref('')
	const goodsId=ref('')
	//跳转首页
	const toHome=()=>{
		uni.switchTab({
			url:"../index/index"
		})
	}
	//电话咨询
	const callPhone=()=>{
		uni.makePhoneCall({
			phoneNumber:phone.value,
			success:(res)=>{},
			fail:(res)=>{
				
			}
		})
	}
	onLoad((options)=>{
		const goods = JSON.parse(options.goods)
		console.log(goods)
		goodsId.value = goods.goodsId;
		//物品图片：轮播图数据
		if (goods.image) {
		swipperList.value = goods.image.split(",");
		}
		goodsName.value = goods.goodsName;
		goodsDesc.value = goods.goodsDesc;
		address.value = goods.address;
		goodsPrice.value = goods.goodsPrice
		createTime.value = goods.createTime
		phone.value = goods.phone
		wxNum.value = goods.wxNum
	})
</script>

<style lang="scss">
	.header {
		display: flex;
	}

	.title {
		color: #303133;
		font-weight: bold;
		padding: 20rpx;
		font-size: 30rpx;
	}

	.price {
		color: #FF7670;
		padding: 20rpx;
		font-size: 30rpx;
	}

	.info-fa {
		display: flex;
		align-items: center;
	}

	.fa-left {
		width: 1px;
		height: 12px;
		border-left: 3px solid #FF7670;
		margin-left: 10px;
	}

	.goodsInfo {
		padding: 5px;
		color: #F3AF28;
	}

	.info {
		display: flex;
		align-items: center;
		background-color: #FFF;
		margin-bottom: 25px;
		padding: 0px 20px;
	}

	.useinfo {
		display: flex;
		flex-direction: column;
		margin-left: 20px;
		margin-bottom: 80px;
	}

	.navigation {
		display: flex;
		margin-top: 100rpx;
		border: solid 2rpx #f2f2f2;
		background-color: #ffffff;
		padding: 16rpx 0;
		position: fixed;
		width: 100%;
		bottom: 0;

		.left {
			display: flex;
			font-size: 20rpx;

			.item {
				margin: 0 30rpx;

				&.car {
					text-align: center;
					position: relative;

					.car-num {
						position: absolute;
						top: -10rpx;
						right: -10rpx;
					}
				}
			}
		}

		.right {
			display: flex;
			font-size: 28rpx;
			align-items: center;

			.btn {
				line-height: 66rpx;
				padding: 0 30rpx;
				border-radius: 36rpx;
				color: #ffffff;
			}

			.cart {
				background-color: #FF7670;
				margin-right: 30rpx;
			}

			.buy {
				background-color: #F3AF28;
			}
		}
	}
</style>