<template>
	<view class="u-wrap">
		<!-- 轮播图 -->
		<swiper class="swiper-container" circular :indicatorColor="indicatorColor" :indicator-dots="indicatorDots"
			:autoplay="autoplay" :interval="interval" :duration="duration">
			<swiper-item v-for="(item,index) in swipperList" :key='index'>
				<image class="imgs" :src="item.images.split(',')[0]"></image>
			</swiper-item>
		</swiper>
		<!-- 搜索框 -->
		<view class="tab-strickt">
			<u-search @change="searchList" v-model="keywords" bg-color="#FFF" margin="8px" style="flex-grow: 1;"
				:show-action="true" action-text="搜索" :animation="true"></u-search>
		</view>
		<view class="">
			<u-waterfall v-if="flowList.length > 0" v-model="flowList" ref="uWaterfall1">
				<template v-slot:left="{leftList}">
					<view class="demo-warter-left" v-for="(item, index) in
leftList" :key="index">
						<!-- 警告：微信小程序中需要hx2.8.11版本才支持在template中结合其
他组件，比如下方的lazy-load组件 -->
						<u-lazy-load @click="toDetail(item)" threshold="-450" border-radius="10" :image="item.image" :index="index">
						</u-lazy-load>
						<view class="demo-title">
							{{item.goodsName}}
						</view>
						<view class="demo-price">
							{{item.goodsPrice}}元
						</view>
						<view class="demo-shop">
							{{item.address}}
						</view>
						<view class="demo-tag">
							<view v-if="item.type == '0'" class="demo-tag-owner">
								闲置
							</view>
							<view style="margin-left: 0;" v-else class="demo-tag-text">
								求购
							</view>
						</view>
					</view>
				</template>
				<template v-slot:right="{rightList}">
					<view class="demo-warter-right" v-for="(item, index) in
rightList" :key="index">
						<u-lazy-load @click="toDetail(item)" threshold="-450" border-radius="10" :image="item.image" :index="index">
						</u-lazy-load>
						<view class="demo-title">
							{{item.goodsName}}
						</view>
						<view class="demo-price">
							{{item.goodsPrice}}元
						</view>
						<view class="demo-shop">
							{{item.address}}
						</view>
						<view class="demo-tag">
							<view v-if="item.type == '0'" class="demo-tag-owner">
								闲置
							</view>
							<view style="margin-left: 0;" v-else class="demo-tag-text">
								求购
							</view>
						</view>
					</view>
				</template>
			</u-waterfall>
			<u-loadmore @loadmore="loadMore" bg-color="rgb(240, 240, 240)" :status="loadStatus"></u-loadmore>
		</view>
	</view>
</template>
<script setup>
	import {
		onReady,
		onReachBottom,onLoad,onShow
	} from '@dcloudio/uni-app';
	import {
		ref
	} from 'vue';
	import {
		getIndexBannerApi,
		getIndexListApi
	} from '../../api/index.js'
	const indicatorDots = ref(true)
	const indicatorColor = ref("#FFF")
	const autoplay = ref(true)
	const interval = ref(2000)
	const duration = ref(500)
	//轮播图数据
	const swipperList = ref([])
	//瀑布流
	const flowList = ref([])
	//首页轮播图
	const getBannerList = async () => {
		let res = await getIndexBannerApi()
		if (res && res.code == 200) {
			console.log(res)
			swipperList.value = res.data;
		}
	}
	//首页推荐
	const loadStatus = ref('loadmore')
	//当前页数
	const currentPage = ref(1)
	//每页查询条数
	const pageSize = ref(10)
	//总页数
	const pages = ref(0)
	const keywords = ref('')
	const getIndexList = async () => {
		let res = await getIndexListApi({
			currentPage: currentPage.value,
			pageSize: pageSize.value,
			keywords: keywords.value
		})
		if (res && res.code == 200) {
			//设置总页数
			pages.value = res.data.pages
			flowList.value = flowList.value.concat(res.data.records);
			loadStatus.value = 'loadmore';
		}
	}
	//加载更多
	const loadMore = () => {
		console.log('点击加载更多')
		// 如果当前页数大于等于总页数，状态修改为没有更多了，不再继续往下执行代码
		if (currentPage.value >= pages.value) {
			loadStatus.value = 'nomore';
			return;
		};
		loadStatus.value = 'loading'; //状态改为加载中
		currentPage.value = ++currentPage.value
		//修改页数后，重新获取数据
		getIndexList()
	}
	//触底加载
	onReachBottom(() => {
		console.log('触底加载更多')
		// 如果当前页数大于等于总页数，状态修改为没有更多了，不再继续往下执行代码
		if (currentPage.value >= pages.value) {
			loadStatus.value = 'nomore';
			return;
		};
		loadStatus.value = 'loading'; //状态改为加载中
		currentPage.value = ++currentPage.value
		//修改页数后，重新获取数据
		getIndexList()
	})
	//搜索
	const uWaterfall1 = ref()
	const searchList = () => {
		uWaterfall1.value.clear()
		currentPage.value = 1;
		loadStatus.value = 'loading';
		getIndexList()
	}
	//跳转详情页
	const toDetail=(item)=>{
			
		if(item.type=='0'){
			uni.navigateTo({
				url:"../unused_detail/unused_detail?goods="+JSON.stringify(item)
			})
		}
		else{
			uni.navigateTo({
				url:"../buy_detail/buy_detail?goods="+JSON.stringify(item)
			})
		}
	}
	onReady(() => {
		getBannerList()
		getIndexList()
	})
	// onLoad(async () => {
	//   await getBannerList(); // 确保分类数据加载完成
	//   getIndexList();       // 初次加载商品列表
	// });
	// 在 onShow 中处理页面重新进入时的刷新
	onShow(() => {
	  currentPage.value = 1;       // 重置为第一页
	  uWaterfall1.value.clear();   // 清空瀑布流数据
	  loadStatus.value = 'loading';// 设置加载状态
	  getIndexList();               // 重新加载最新数据
	});
</script>
<style lang='scss'>
	.swiper-container {
		height: 180px;

		.item {
			height: 180px;
		}

		.imgs {
			height: 180px;
			width: 100%;
		}
	}

	.demo-warter-left {
		border-radius: 8px;
		margin: 5px 0px 5px 5px;
		background-color: #ffffff;
		padding: 8px;
		position: relative;
	}

	.demo-warter-right {
		border-radius: 8px;
		margin: 5px 5px 5px 0px;
		background-color: #ffffff;
		padding: 8px;
		position: relative;
	}

	.u-close {
		position: absolute;
		top: 32rpx;
		right: 32rpx;
	}

	.demo-image {
		width: 100%;
		border-radius: 4px;
	}

	.demo-title {
		font-size: 30rpx;
		margin-top: 5px;
		color: $u-main-color;
	}

	.demo-tag {
		display: flex;
		margin-top: 5px;
	}

	.demo-tag-owner {
		background-color: $u-type-error;
		color: #FFFFFF;
		display: flex;
		align-items: center;
		padding: 4rpx 14rpx;
		border-radius: 50rpx;
		font-size: 20rpx;
		line-height: 1;
	}

	.demo-tag-text {
		border: 1px solid $u-type-primary;
		color: $u-type-primary;
		margin-left: 10px;
		border-radius: 50rpx;
		line-height: 1;
		padding: 4rpx 14rpx;
		display: flex;
		align-items: center;
		border-radius: 50rpx;
		font-size: 20rpx;
	}

	.demo-price {
		font-size: 30rpx;
		color: $u-type-error;
		margin-top: 5px;
	}

	.demo-shop {
		font-size: 22rpx;
		color: $u-tips-color;
		margin-top: 5px;
	}

	.tab-strickt {
		position: sticky;
		z-index: 99;
		top: 0;
		left: 0;
		display: flex;
		align-items: center;
		background-color: #f2f2f2;
	}
</style>