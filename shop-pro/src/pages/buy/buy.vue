<template>
	<view class="u-wrap">
		<u-search margin="20rpx 0rpx":show-action="true" action-text="搜索" :animation="true"></u-search>
	</view>
	<view class="tab-strickt">
		 <u-tabs active-color="#FF7670" name="cate_name" count="cate_count" :list="tabList" :is-scroll="true" v-model="current" @change="change"></u-tabs>
	</view>
	<view class="">
		<u-waterfall v-model="flowList" ref="uWaterfall1">
			<template v-slot:left="{leftList}">
				<view class="demo-warter-left" v-for="(item, index) in leftList" :key="index">
					<!-- 警告：微信小程序中需要hx2.8.11版本才支持在template中结合其他组件，比如下方的lazy-load组件 -->
					<u-lazy-load threshold="-450" border-radius="10" :image="item.image"
						:index="index"></u-lazy-load>
					<view class="demo-title">
						{{item.title}}
					</view>
					<view class="demo-price">
						{{item.price}}元
					</view>
					<view class="demo-tag">
						<view class="demo-tag-owner">
							自营
						</view>
						<view class="demo-tag-text">
							放心购
						</view>
					</view>
					<view class="demo-shop">
						{{item.shop}}
					</view>
					<u-icon name="close-circle-fill" color="#fa3534" size="34" class="u-close"
						@click="remove(item.id)"></u-icon>
				</view>
			</template>
			<template v-slot:right="{rightList}">
				<view class="demo-warter-right" v-for="(item, index) in rightList" :key="index">
					<u-lazy-load threshold="-450" border-radius="10" :image="item.image"
						:index="index"></u-lazy-load>
					<view class="demo-title">
						{{item.title}}
					</view>
					<view class="demo-price">
						{{item.price}}元
					</view>
					<view class="demo-tag">
						<view class="demo-tag-owner">
							自营
						</view>
						<view class="demo-tag-text">
							放心购
						</view>
					</view>
					<view class="demo-shop">
						{{item.shop}}
					</view>
					<u-icon name="close-circle-fill" color="#fa3534" size="34" class="u-close"
						@click="remove(item.id)"></u-icon>
				</view>
			</template>
		</u-waterfall>
		<u-loadmore bg-color="rgb(240, 240, 240)" :status="loadStatus" @loadmore="addRandomData"></u-loadmore>
	</view>
</template>

<script setup>
import { ref } from 'vue';

	const tabList=ref([
		{
			cate_name:'全部'
		},
		{
			cate_name:'手机'
		},
		{
			cate_name:'电脑'
		},
		{
			cate_name:'衣服'
		},
		{
			cate_name:'鞋子'
		},
		{
			cate_name:'图书'
		},
		{
			cate_name:'时尚'
		},
		{
			cate_name:'手作'
		},
	])
	const current=ref(0)
	const flowList = ref([{
			price: 7500,
			title: "笔记本电脑",
			image: '/static/11.jpeg',
		},
		{
			price: 3000,
			title: "手机",
			image: '/static/22.jpg',
		},
		{
			price: 7500,
			title: "电脑",
			image: '/static/55.jpeg',
		},
	])
</script>

<style lang="scss">
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
	/*去掉tabs选项卡滚动条*/
	scroll-view ::v-deep ::-webkit-scrollbar {
	width: 0;
	height: 0;
	color: transparent
	}
</style>
