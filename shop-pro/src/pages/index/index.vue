<template>
	<view class="u-wrap">
		<!-- 轮播图 -->
		<swiper class="swiper-container" circular :indicatorColor="indicatorColor" :indicator-dots="indicatorDots"
			:autoplay="autoplay" :interval="interval" :duration="duration">
			<swiper-item @click="toDetail(item)" v-for="(item,index) in swipperList" :key='index'>
				<image class="imgs" :src="item.image"></image>
			</swiper-item>
		</swiper>
		<!-- 搜索框 -->
		<view class="tab-strickt">
			<u-search @change="searchList" v-model="keywords" bg-color="#FFF" margin="8px" style="flex-grow: 1;"
				:show-action="true" action-text="搜索" :animation="true"></u-search>
		</view>
        
        <!-- 分类图标 -->
        <view class="category-list" v-if="categoryList.length > 0">
            <view class="category-item" v-for="(item, index) in categoryList" :key="index" @click="selectCategory(item)">
                <image :src="item.icon || '/static/images/category/default.png'" mode="aspectFill"></image>
                <text>{{ item.label }}</text>
            </view>
        </view>
        
        <!-- 推荐商品标题 -->
        <view class="section-header" v-if="isRecommendVisible">
            <text class="section-title">为你推荐</text>
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
		ref, computed
	} from 'vue';
	import {
		getIndexBannerApi,
		getIndexListApi
	} from '../../api/index.js';
    import { searchApi, behaviorApi } from '../../api/api.js';
    
	const indicatorDots = ref(true)
	const indicatorColor = ref("#FFF")
	const autoplay = ref(true)
	const interval = ref(2000)
	const duration = ref(500)
	//轮播图数据
	const swipperList = ref([])
	//瀑布流
	const flowList = ref([])
    // 分类列表
    const categoryList = ref([])
    // 推荐商品
    const recommendGoods = ref([])
    // 当前选中的分类ID
    const selectedCategoryId = ref(null)
    // 用户信息
    const userInfo = ref(null)
    // 是否显示推荐标题
    const isRecommendVisible = computed(() => !keywords.value && !selectedCategoryId.value)
    
	//首页轮播图
	const getBannerList = async () => {
		let res = await getIndexBannerApi()
		if (res && res.code == 200) {
			console.log(res)
			swipperList.value = res.data;
		}
	}
    
    // 加载分类
    const loadCategories = async () => {
        try {
            // 从本地存储获取分类数据
            const categories = uni.getStorageSync('categoryList');
            if (categories) {
                categoryList.value = JSON.parse(categories);
            } else {
                // 如果没有缓存数据，可以从API获取
                // 这里需要调用您的分类API
                // const res = await getCategoryListApi();
                // categoryList.value = res.data || [];
                // uni.setStorageSync('categoryList', JSON.stringify(categoryList.value));
                
                // 临时示例数据
                categoryList.value = [
                    { id: 1, label: '数码', icon: '/static/images/category/digital.png' },
                    { id: 2, label: '服装', icon: '/static/images/category/clothing.png' },
                    { id: 3, label: '家居', icon: '/static/images/category/home.png' },
                    { id: 4, label: '美妆', icon: '/static/images/category/beauty.png' },
                    { id: 5, label: '图书', icon: '/static/images/category/book.png' }
                ];
                uni.setStorageSync('categoryList', JSON.stringify(categoryList.value));
            }
        } catch (e) {
            console.error('加载分类数据失败', e);
        }
    }
    
    // 获取用户信息
    const getUserInfo = () => {
        try {
            const info = uni.getStorageSync('userInfo');
            if (info) {
                userInfo.value = typeof info === 'string' ? JSON.parse(info) : info;
            }
        } catch (e) {
            console.error('获取用户信息失败', e);
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
        // 判断是否有关键词或分类过滤
        if (keywords.value || selectedCategoryId.value) {
            // 使用Elasticsearch搜索API
            try {
                const params = {
                    keyword: keywords.value,
                    categoryId: selectedCategoryId.value,
                    page: currentPage.value,
                    size: pageSize.value
                };
                
                const res = await searchApi.searchGoods(params);
                
                if (res && res.code == 200) {
                    if (currentPage.value === 1) {
                        flowList.value = res.data.content || [];
                    } else {
                        flowList.value = flowList.value.concat(res.data.content || []);
                    }
                    
                    // 更新总页数
                    pages.value = res.data.totalPages || 1;
                    
                    // 更新加载状态
                    loadStatus.value = res.data.last ? 'nomore' : 'loadmore';
                }
            } catch (error) {
                console.error('搜索商品失败', error);
                loadStatus.value = 'loadmore';
            }
        } else {
            // 未指定关键词和分类，显示推荐和普通列表
            if (currentPage.value === 1) {
                // 第一页先加载推荐商品
                await loadRecommendGoods();
            }
            
            // 然后加载普通商品列表
            let res = await getIndexListApi({
                currentPage: currentPage.value,
                pageSize: pageSize.value
            });
            
            if (res && res.code == 200) {
                // 设置总页数
                pages.value = res.data.pages;
                
                if (currentPage.value === 1) {
                    // 第一页，如果有推荐商品，与推荐商品合并
                    if (recommendGoods.value.length > 0) {
                        // 确保不重复
                        const recommendIds = recommendGoods.value.map(item => item.goodsId);
                        const filteredRecords = res.data.records.filter(item => !recommendIds.includes(item.goodsId));
                        
                        flowList.value = [...recommendGoods.value, ...filteredRecords];
                    } else {
                        flowList.value = res.data.records;
                    }
                } else {
                    // 非第一页，直接追加
                    flowList.value = flowList.value.concat(res.data.records);
                }
                
                loadStatus.value = 'loadmore';
            }
        }
	}
    
    // 加载推荐商品
    const loadRecommendGoods = async () => {
        try {
            // 如果用户已登录，获取个性化推荐
            if (userInfo.value && userInfo.value.userId) {
                const res = await searchApi.getRecommendGoods(userInfo.value.userId, 6);
                if (res && res.code == 200) {
                    recommendGoods.value = res.data || [];
                }
            } else {
                // 未登录用户获取热门推荐
                const res = await searchApi.getHotGoods(6);
                if (res && res.code == 200) {
                    recommendGoods.value = res.data || [];
                }
            }
        } catch (error) {
            console.error('加载推荐商品失败', error);
            recommendGoods.value = [];
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
		if (uWaterfall1.value) {     // 添加检查
		        uWaterfall1.value.clear()
		    }
		currentPage.value = 1;
        selectedCategoryId.value = null; // 清除分类筛选
		loadStatus.value = 'loading';
		getIndexList()
	}
    
    // 选择分类
    const selectCategory = (category) => {
        if (uWaterfall1.value) {     // 添加检查
                uWaterfall1.value.clear()
            }
        currentPage.value = 1;
        // 如果是同一个分类，则取消选择
        selectedCategoryId.value = selectedCategoryId.value === category.id ? null : category.id;
        loadStatus.value = 'loading';
        getIndexList()
    }
    
	//跳转详情页
	const toDetail = (item) => {
        // 记录浏览行为
        if (userInfo.value && userInfo.value.userId) {
            behaviorApi.recordView(userInfo.value.userId, item.goodsId);
        }
        
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
        getUserInfo()
        loadCategories()
		getIndexList()
	})
    
	// 在 onShow 中处理页面重新进入时的刷新
	onShow(() => {
        // 重新获取用户信息，以防用户在其他页面登录
        getUserInfo()
        // 重置页面状态
        currentPage.value = 1;       // 重置为第一页
        if (uWaterfall1.value) {     // 添加检查，确保ref已绑定
                uWaterfall1.value.clear();   // 清空瀑布流数据
            }
            loadStatus.value = 'loading';// 设置加载状态
            getIndexList();               // 重新加载最新数据         // 重新加载最新数据
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

    .category-list {
        display: flex;
        justify-content: space-around;
        padding: 20rpx 0;
        background-color: #ffffff;
        margin-bottom: 20rpx;
    }

    .category-item {
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .category-item image {
        width: 90rpx;
        height: 90rpx;
        border-radius: 50%;
        margin-bottom: 10rpx;
    }

    .category-item text {
        font-size: 22rpx;
        color: #333333;
    }
    
    .section-header {
        padding: 20rpx 30rpx;
        background-color: #ffffff;
        margin-bottom: 10rpx;
    }

    .section-title {
        font-size: 32rpx;
        font-weight: bold;
        color: #333333;
        border-left: 8rpx solid #ff6700;
        padding-left: 20rpx;
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