<template>
	<view class="u-wrap">
		<swiper class="swiper-container" circular :indicatorColor="indicatorColor" :indicator-dots="indicatorDots"
			:autoplay="autoplay" :interval="interval" :duration="duration">
			<swiper-item v-for="(item,index) in swipperList">
				<image class="imgs" :src="item.image"></image>
			</swiper-item>
		</swiper>
		<view class="tab-strickt">
			<u-search bg-color="#FFF" margin="8px" style ="flex-grow: 1;":show-action="true" action-text="搜索" :animation="true"></u-search>
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
	</view>
</template>

<script setup>
	import {
		ref
	} from 'vue';

	const indicatorDots = ref(true)
	const indicatorColor = ref("#FFF")
	const autoplay = ref(true)
	const interval = ref(2000)
	const duration = ref(500)
	const swipperList = ref([{
			image: '/static/33.jpg'
		},
		{
			image: '/static/44.jpg'
		},
		{
			image: '/static/66.jpg'
		},
	])
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