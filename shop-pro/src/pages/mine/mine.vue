<template>
	<view>
		<view @click="toUserInfo" class="u-flex user-box u-p-30">
			<view class="u-m-r-10">
				<u-avatar v-if="picture" :src="picture" size="100"></u-avatar>
				<u-avatar v-else src="/static/user.jpg" size="100"></u-avatar>
			</view>
			<view class="u-flex-1">
				<view class="u-font-18 u-p-b-20">{{nickName}}</view>
			</view>
			<view class="u-m-l-10 u-p-10">
				<u-icon name="arrow-right" color="#969799" size="28"></u-icon>
			</view>
		</view>
		<view class="u-m-t-20">
			<u-cell-group>
				<u-cell-item @click="toMyUnused" icon="star" title="我的闲置"></u-cell-item>
					<u-cell-item @click="toMyBy" icon="photo" title="我的求购"></u-cell-item>
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
				<u-cell-item @click="loginOut" icon="setting" title="退出账号">
				</u-cell-item>
			</u-cell-group>
		</view>
	</view>
</template>
<script setup>
	// import logo form '../../static/'
	import {
		ref
	} from 'vue';
	import {
		onShow
	} from '@dcloudio/uni-app'
	import {
		getInfoApi
	} from '../../api/user.js'
	const pic = ref('https://uviewui.com/common/logo.png')
	const show = ref(true)
	//我的闲置
	const toMyUnused = () => {
		uni.navigateTo({
			url: "../my_unused/my_unused"
		})
	}
	//我的求购
	const toMyBy = () => {
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
		uni.reLaunch({
			url: "../login/login"
		})
	}
	//昵称
	const nickName = ref('kika')
	//头像
	const picture = ref('')
	//用户信息
	let userInfo = null;
	//获取个人信息
	const getInfo = async () => {
		let res = await getInfoApi({
			userId: uni.getStorageSync("userId")
		})
		if (res && res.code == 200) {
			console.log(res)
			nickName.value = res.data.nickName
			picture.value = res.data.picture
			userInfo = res.data;
		}
	}
	//修改个人信息
	const toUserInfo = () => {
		uni.navigateTo({
			url: "../user_info/user_info?userInfo=" + JSON.stringify(userInfo)
		})
	}
	onShow(() => {
		getInfo()
	})
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