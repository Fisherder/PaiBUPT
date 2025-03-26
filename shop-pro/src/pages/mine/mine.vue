<template>
	<view>
		<view class="u-flex user-box u-p-30">
			<view class="u-m-r-10">
				<u-avatar :src=pic size="100"></u-avatar>
			</view>
			<view class="u-flex-1">
				<view class="u-font-18 u-p-b-20">kika</view>
			</view>
			<view class="u-m-l-10 u-p-10">
				<u-icon name="arrow-right" color="#969799" size="28"></u-icon>
			</view>
		</view>
		<view class="u-m-t-20">
			<u-cell-group>
				<u-cell-item @click="toUnused" icon="star" title="我的闲置"></u-cell-item>
					<u-cell-item @click="toMyBuy" icon="photo" title="我的求购"></u-cell-item>
						<u-cell-item @click="toCollect" icon="heart" title="我的收藏"></u-cell-item>
							<u-cell-item @click="toOrder" icon="red-packet" title="购买订单">
							</u-cell-item>
							<u-cell-item @click="toSellOrder" icon="order" title="出售订单">
							</u-cell-item>
							<u-cell-item @click="toUpdate" icon="edit-pen" title="修改密码">
							</u-cell-item>
			</u-cell-group>
		</view>

		<view class="u-m-t-20">
			<u-cell-group>
				<u-cell-item @click="loginOut" icon="setting" title="退出账号"></u-cell-item>
			</u-cell-group>
		</view>
	</view>
</template>

<script setup>
	import {
		ref
	} from 'vue';
	const pic = ref('/static/user.jpg')
	const show = ref(true)

	//我的闲置
	const toUnused = () => {
		uni.navigateTo({
			url: "../my_unused/my_unused"
		})
	}
	//我的求购
	const toMyBuy = () => {
		uni.navigateTo({
			url: "../my_buy/my_buy"
		})
	}
	//我的收藏
	const toCollect = () => {
		uni.navigateTo({
			url: "../my_collect/my_collect"
		})
	}
	//购买订单
	const toOrder = () => {
		uni.navigateTo({
			url: "../my_order/my_order"
		})
	}
	//出售订单
	const toSellOrder = () => {
		uni.navigateTo({
			url: "../sell_order/sell_order"
		})
	}
	//修改密码
	const toUpdate = () => {
		uni.navigateTo({
			url: "../update_password/update_password"
		})
	}

	//退出登录
	const loginOut = () => {
		uni.clearStorageSync()
		uni.navigateTo({
			url: "../login/login"
		})
	}
</script>

<style lang="scss">
	page {
		background-color: #ededed;
	}

	.camera {
		width: 54px;
		height: 44px;

		&:active {
			background-color: #ededed;
		}
	}

	.user-box {
		background-color: #fff;
	}
</style>