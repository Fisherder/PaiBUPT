<template>
	<view class="u-wrap">
		<!-- 搜索框 -->
		<u-search @change="searchList" v-model="keywords" margin="20rpx 0rpx" :show-action="true" action-text="搜索" :animation="true"></u-search>
		<view class="tab-strickt">
			<u-tabs active-color="#FF7670" name="categoryName" count="cate_count" :list="tabList" :is-scroll="true"
				v-model="current" @change="change"></u-tabs>
		</view>
		<!-- 瀑布流列表 -->
		<view class="">
			<u-waterfall v-model="flowList" ref="uWaterfall1">
				<template v-slot:left="{leftList}">
					<view class="demo-warter" v-for="(item, index) in leftList" :key="index">
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
					<view class="demo-warter" v-for="(item, index) in rightList" :key="index">
						<u-lazy-load @click="toDetail(item)" threshold="-450" border-radius="10" :image="item.image" :index="index">
						</u-lazy-load>
						<view class="demo-title">
							{{item.goodsName}}
						</view>
						<view class="demo-price">
							{{item.goodsPrice}}元
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
		</view>
		<u-loadmore bg-color="rgb(240, 240, 240)" :status="loadStatus" @loadmore="loadMore"></u-loadmore>
	</view>
</template>
<script setup>
	import {
		onReady,onReachBottom,onLoad,onShow
	} from '@dcloudio/uni-app';
	import {
		ref
	} from 'vue';
	import {
		getCateListApi,getBuyListApi
	} from '../../api/goods.js'
	const tabList = ref([])
	const current = ref(0)
	const uWaterfall1=ref()
	const loadStatus = ref('loadmore')
	//瀑布流
	const flowList = ref([])
	//获取分类数据
	const getCateList = async () => {
		let res = await getCateListApi()
		if (res && res.code == 200) {
			console.log(res)
			tabList.value = res.data;
			//分类数据的第一位添加全部
			tabList.value.unshift({
				categoryId: '',
				categoryName: '全部',
				orderNum: 0
			})
		}
	}
	//分类点击事件
	const categoryId = ref('')
	const change = (e) => {
		categoryId.value = tabList.value[e].categoryId
		console.log(categoryId.value)
		//清空列表数据
		currentPage.value = 1;
		//清空瀑布流的数据
		uWaterfall1.value.clear()
		//调用列表
		getUsedList()

	}
	//获取闲置列表数据
	const currentPage=ref(1)
	const pageSize=ref(10)
	const keywords=ref('')
	const pages=ref(0)
	const getUsedList=async()=>{
		let res=await getBuyListApi({
			currentPage:currentPage.value,
			pageSize:pageSize.value,
			categoryId:categoryId.value,
			keywords:keywords.value
		})
		if(res && res.code==200){
			console.log(res)
			flowList.value=flowList.value.concat(res.data.records)
			pages.value=res.data.pages
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
		getUsedList()
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
		getUsedList()
	})
	//搜索
	const searchList = () => {
		uWaterfall1.value.clear()
		currentPage.value = 1;
		loadStatus.value = 'loading';
		getUsedList()
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
		getCateList()
		getUsedList()
	})
	// onLoad(async () => {
	//   await getCateList(); // 确保分类数据加载完成
	//   getUsedList();       // 初次加载商品列表
	// });
	// 在 onShow 中处理页面重新进入时的刷新
	onShow(() => {
	  currentPage.value = 1;       // 重置为第一页
	  uWaterfall1.value.clear();   // 清空瀑布流数据
	  loadStatus.value = 'loading';// 设置加载状态
	  getUsedList();               // 重新加载最新数据
	});
</script>
<style lang="scss">
	.tab-strickt {
		position: sticky;
		z-index: 99;
		top: 0;
		left: 0;
	}

	.demo-warter {
		border-radius: 8px;
		margin: 5px;
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

	.tab-sticky {
		position: sticky;
		z-index: 99;
		top: 0;
		left: 0;
		background-color: #f2f2f2;
		display: flex;
		align-items: center;
	}

	/*去掉tabs选项卡滚动条*/
	scroll-view ::v-deep ::-webkit-scrollbar {
		width: 0;
		height: 0;
		color: transparent
	}

	
</style>