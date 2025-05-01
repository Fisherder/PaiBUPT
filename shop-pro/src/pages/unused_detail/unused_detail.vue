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
		
		<!-- 添加猜你喜欢区域 -->
		<view class="info-fa" v-if="similarGoods.length > 0">
			<view class="fa-left"></view>
			<view class="goodsInfo">
				猜你喜欢
			</view>
		</view>
		<view class="similar-goods" v-if="similarGoods.length > 0">
			<scroll-view scroll-x="true" class="scroll-view">
				<view class="similar-item" v-for="(item, index) in similarGoods" :key="index" @click="toOtherDetail(item)">
					<image :src="item.image" mode="aspectFill"></image>
					<view class="similar-name">{{item.goodsName}}</view>
					<view class="similar-price">￥{{item.goodsPrice}}</view>
				</view>
			</scroll-view>
		</view>
		
		<view class="navigation">
			<view class="left">
				<view @click="toHome" class="item">
					<u-icon name="home" :size="40"></u-icon>
					<view class="text u-line-1">首页</view>
				</view>
				<view @click="collectBtn" class="item">
					<u-icon v-if="hasStatus == '0'" :size="40" name="star"></u-icon>
					<u-icon v-if="hasStatus == '1'" color="#FF7670" :size="40" name="star"></u-icon>
					<view v-if="hasStatus == '0'" class="text u-line-1">收藏
					</view>
					<view v-if="hasStatus == '1'" style="color:#FF7670" class="text u-line-1">收藏</view>
				</view>
				<!-- <view class="item car">
					<u-icon name="info-circle" :size="40"></u-icon>
					<view class="text u-line-1">举报</view>
				</view> -->
			</view>
			<view class="right">
				<view @click="callPhone" class="cart btn u-line-1">电话咨询</view>
				<view @click="toBuy" class="buy btn u-line-1">拍卖出价</view>
			</view>
		</view>
		<!-- 拍卖出价 -->
		<u-popup :mask-close-able="false" border-radius="15" width="85%" height="200px" v-model="show" mode="center">
			<view style="padding:50px 15px 30px 15px;">
				<u-form label-width="auto" :model="addModel" ref="form1">
					<u-form-item label="先前报价">
						<view>
							{{goodsPrice}}
						</view>
					</u-form-item>
					<u-form-item label="截止时间">
						<view>
							{{deadline}}
						</view>
					</u-form-item>
					<u-form-item label="当前出价最高者">
						<view>
							{{maxUser}}
						</view>
					</u-form-item>
					<u-form-item label="请输入出价" prop="price"><u-input v-model="addModel.price" />
					</u-form-item>
				</u-form>
			</view>
			<view class="conBtn">
				<u-button @click="cancel" style="margin-right: 15px;" type="info">取消交易</u-button>
				<u-button @click="confirm" style="margin-left: 15px;" :custom-style="customStyle"
					type="error">确定交易</u-button>
			</view>
		</u-popup>
	</view>
</template>

<script setup>
	import {
		onLoad, onMounted
	} from '@dcloudio/uni-app';
	import {
		ref,
		reactive,
		computed
	} from 'vue';
	import {
		replaceOrderApi,
		getGoodsDetailApi
	} from '../../api/order';
	import {
		collectApi,
		hasCollectApi
	} from '../../api/unused';
	import { behaviorApi, searchApi } from '../../api/api'; // 引入行为API和搜索API
	
	const customStyle = reactive({
		background: '#FF7670'
	})
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
	// 计算属性：createTime + 48 小时
	const deadline = computed(() => {
		if (!createTime.value) return '';

		// 将 createTime 转换为 Date 对象
		const originTime = new Date(createTime.value);

		// 添加 48 小时（48 * 60 * 60 * 1000 毫秒）
		const newTime = new Date(originTime.getTime() + 48 * 60 * 60 * 1000);

		// 格式化时间（示例格式：YYYY-MM-DD HH:mm:ss）
		// 使用 uView 的日期格式化工具（需确保已配置）
		return uni.$u.date(newTime, 'yyyy-mm-dd hh:MM:ss');
	});
	const goodsId = ref('')
	// 相似商品列表
	const similarGoods = ref([])
	// 用户信息
	const userInfo = ref(null)
	
	// 获取用户信息
	const getUserInfo = () => {
		try {
			// 获取用户信息
			const userId = uni.getStorageSync('userId');
			const username = uni.getStorageSync('username');
			if (userId) {
				userInfo.value = {
					userId: userId,
					username: username
				};
			}
		} catch (e) {
			console.error('获取用户信息失败', e);
		}
	}
	
	// 记录浏览行为
	const recordViewBehavior = async () => {
		try {
			if (userInfo.value && userInfo.value.userId && goodsId.value) {
				await behaviorApi.recordView(userInfo.value.userId, goodsId.value);
				console.log('浏览行为记录成功');
			}
		} catch (error) {
			console.error('浏览行为记录失败', error);
		}
	}
	
	// 获取相似商品推荐
	const getSimilarGoods = async () => {
		try {
			// 调用相似商品API
			const params = {
				goodsId: goodsId.value,
				limit: 10 // 限制推荐数量
			};
			
			// 如果用户已登录，传入用户ID以获取个性化推荐
			if (userInfo.value && userInfo.value.userId) {
				params.userId = userInfo.value.userId;
			}
			
			// 调用相似商品推荐API
			const res = await searchApi.getSimilarGoods(params);
			if (res && res.code == 200) {
				// 过滤掉当前商品
				similarGoods.value = (res.data || []).filter(item => item.goodsId != goodsId.value);
			}
		} catch (error) {
			console.error('获取相似商品失败', error);
		}
	}
	
	//跳转首页
	const toHome = () => {
		uni.switchTab({
			url: "../index/index"
		})
	}
	
	// 跳转到其他商品详情
	const toOtherDetail = (item) => {
		// 记录浏览行为
		if (userInfo.value && userInfo.value.userId) {
			behaviorApi.recordView(userInfo.value.userId, item.goodsId);
		}
		
		// 跳转到详情页
		if (item.type == '0') {
			uni.navigateTo({
				url: "../unused_detail/unused_detail?goods=" + JSON.stringify(item)
			})
		} else {
			uni.navigateTo({
				url: "../buy_detail/buy_detail?goods=" + JSON.stringify(item)
			})
		}
	}
	
	//收藏按钮
	const collectBtn = async () => {
		try {
			let res = await collectApi({
				userId: uni.getStorageSync("userId"),
				goodsId: goodsId.value
			})
			if (res && res.code == 200) {
				console.log(res)
				
				// 记录收藏行为
				if (userInfo.value && userInfo.value.userId) {
					// 根据当前收藏状态决定是记录收藏还是取消收藏
					const newStatus = hasStatus.value === '0' ? '1' : '0';
					
					if (newStatus === '1') {
						// 记录收藏行为
						await behaviorApi.recordFavorite(userInfo.value.userId, goodsId.value);
						console.log('收藏行为记录成功');
					}
				}
				
				hasCollect()
			}
		} catch (error) {
			console.error('收藏操作失败', error);
		}
	}
	
	const hasStatus = ref('0')
	//查询是否收藏
	const hasCollect = async () => {
		let res = await hasCollectApi({
			userId: uni.getStorageSync("userId"),
			goodsId: goodsId.value
		})
		if (res && res.code == 200) {
			console.log(res)
			hasStatus.value = res.data
		}
	}
	//电话咨询
	const callPhone = () => {
		uni.makePhoneCall({
			phoneNumber: phone.value,
			success: (res) => {},
			fail: (res) => {

			}
		})
	}
	//商品创建人ID
	const creatUser = ref('')
	//当前出价最高者
	const maxUser = ref('')
	//拍卖出价
	const show = ref(false)
	const toBuy = () => {
		show.value = true;
	}
	const addModel = reactive({
		price: "",
		goodsId: "",
		orderUser: uni.getStorageSync("userId"),
	})
	//交易取消
	const cancel = () => {
		show.value = false;
	}
	//交易确定
	const confirm = async () => {
		//是否填写金额
		if (!addModel.price) {
			uni.showToast({
				title: '请填写金额',
				icon: "none",
				mask: true,
				duration: 3000
			})
			return
		}
		//不能自己交易自己的物品
		if (creatUser.value == addModel.orderUser) {
			uni.showToast({
				title: '不能自己交易自己的物品',
				icon: "none",
				mask: true,
				duration: 3000
			})
			return
		}
		
		let res = await replaceOrderApi(addModel)
		if (res && res.code == 200) {
			// 重新获取商品详情数据
			let goodsRes = await getGoodsDetailApi(addModel);
			if (goodsRes && goodsRes.code === 200) {
				let updatedGoods = goodsRes.data;
				// 更新所有相关响应式变量
				maxUser.value = updatedGoods.ownName;
				goodsPrice.value = updatedGoods.goodsPrice;
				
				// 记录购买行为
				if (userInfo.value && userInfo.value.userId) {
					await behaviorApi.recordPurchase(userInfo.value.userId, goodsId.value);
					console.log('购买行为记录成功');
				}
				
				uni.showToast({
					title: '交易成功',
					icon: "success",
					mask: true,
					duration: 3000
				});
				show.value = false;
			}
		}
	}
	
	onLoad(async (options) => {
		const goods = JSON.parse(options.goods)
		console.log(goods)
		goodsId.value = goods.goodsId;
		addModel.goodsId = goods.goodsId;
		creatUser.value = goods.userId;
		//物品图片：轮播图数据
		if (goods.image) {
			swipperList.value = goods.image.split(",");
		}
		goodsName.value = goods.goodsName;
		goodsDesc.value = goods.goodsDesc;
		address.value = goods.address;
		goodsPrice.value = goods.goodsPrice;
		createTime.value = goods.createTime;
		phone.value = goods.phone;
		wxNum.value = goods.wxNum;
		maxUser.value = goods.ownName;
		
		// 获取用户信息
		getUserInfo();
		
		// 查询是否收藏
		hasCollect();
		
		// 记录浏览行为
		await recordViewBehavior();
		
		// 获取相似商品推荐
		await getSimilarGoods();
	})
</script>

<style lang="scss">
	.conBtn {
		display: flex;
		justify-content: center;
		align-items: center;
	}

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
		margin-bottom: 20px;
	}
	
	/* 相似商品样式 */
	.similar-goods {
		background-color: #FFF;
		padding: 20rpx;
		margin-bottom: 80px;
	}
	
	.scroll-view {
		white-space: nowrap;
		width: 100%;
	}
	
	.similar-item {
		display: inline-block;
		width: 200rpx;
		margin-right: 20rpx;
	}
	
	.similar-item image {
		width: 200rpx;
		height: 200rpx;
		border-radius: 10rpx;
	}
	
	.similar-name {
		font-size: 24rpx;
		white-space: normal;
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 2;
		-webkit-box-orient: vertical;
	}
	
	.similar-price {
		font-size: 24rpx;
		color: #FF7670;
		font-weight: bold;
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