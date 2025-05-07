<template>
  <view>
    <view v-if="isLoggedIn" @click="toUserInfo" class="u-flex user-box u-p-30">
      <!-- 登录后的原始用户资料UI -->
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
    
    <!-- 未登录时显示登录提示 -->
    <view v-else @click="toLogin" class="u-flex user-box u-p-30">
      <view class="u-m-r-10">
        <u-avatar src="/static/user.jpg" size="100"></u-avatar>
      </view>
      <view class="u-flex-1">
        <view class="u-font-18 u-p-b-20">点击登录/注册</view>
      </view>
      <view class="u-m-l-10 u-p-10">
        <u-icon name="arrow-right" color="#969799" size="28"></u-icon>
      </view>
    </view>

    <!-- 不需要登录的功能 -->
    <view class="u-m-t-20">
      <u-cell-group>
        <u-cell-item @click="handleFeatureClick('unused')" icon="star" title="闲置列表"></u-cell-item>
        <u-cell-item @click="handleFeatureClick('buy')" icon="photo" title="求购列表"></u-cell-item>
      </u-cell-group>
    </view>

    <!-- 需要登录的功能 -->
    <view v-if="isLoggedIn" class="u-m-t-20">
      <u-cell-group>
        <u-cell-item @click="toMyUnused" icon="star" title="我的闲置"></u-cell-item>
        <u-cell-item @click="toMyBy" icon="photo" title="我的求购"></u-cell-item>
        <u-cell-item @click="toCollect" icon="heart" title="我的收藏"></u-cell-item>
        <u-cell-item @click="toOrder" icon="red-packet" title="购买订单"></u-cell-item>
        <u-cell-item @click="toSellOrder" icon="order" title="出售订单"></u-cell-item>
        <u-cell-item @click="toUpdate" icon="edit-pen" title="修改密码"></u-cell-item>
      </u-cell-group>
    </view>
    
    <!-- 退出登录按钮 -->
    <view v-if="isLoggedIn" class="u-m-t-20">
      <u-cell-group>
        <u-cell-item @click="loginOut" icon="setting" title="退出账号"></u-cell-item>
      </u-cell-group>
    </view>
  </view>
</template>
<script setup>
	import {
		ref, 
		onMounted,
		getCurrentInstance
	} from 'vue';
	import {
		onShow
	} from '@dcloudio/uni-app'
	import {
		getInfoApi
	} from '../../api/user.js'
	
	// 添加isLoggedIn变量声明
	const isLoggedIn = ref(false);
	const pic = ref('https://uviewui.com/common/logo.png')
	const show = ref(true)
	//昵称
	const nickName = ref('点击登录/注册')
	//头像
	const picture = ref('')
	//用户信息
	let userInfo = null;
	
	// 添加用于监听登录状态变化的代码
	const instance = getCurrentInstance();
	
	// 监听登录成功事件
	onMounted(() => {
		// 初始化登录状态
		const userId = uni.getStorageSync("userId");
		isLoggedIn.value = !!userId;
		
		if(isLoggedIn.value) {
			getInfo();
		}
		
		// 如果有事件总线，监听登录事件
		if(instance && instance.proxy && instance.proxy.$eventBus) {
			instance.proxy.$eventBus.on('login-success', () => {
				console.log('检测到登录成功事件');
				isLoggedIn.value = true;
				getInfo();
			});
			
			instance.proxy.$eventBus.on('logout', () => {
				isLoggedIn.value = false;
				nickName.value = '点击登录/注册';
				picture.value = '';
			});
		}
	});
	
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
		// 发布登出事件
		if(instance && instance.proxy && instance.proxy.$eventBus) {
			instance.proxy.$eventBus.emit('logout');
		}
		
		uni.clearStorageSync()
		uni.reLaunch({
			url: "../login/login"
		})
	}
	// 登录重定向
	const toLogin = () => {
		uni.navigateTo({
			url: "../login/login"
		})
	}
	// 处理需要登录的功能点击
	const handleFeatureClick = (feature) => {
		if (!isLoggedIn.value) {
			uni.showModal({
				title: '提示',
				content: '该功能需要登录后才能使用',
				confirmText: '去登录',
				cancelText: '取消',
				success: function(res) {
					if (res.confirm) {
						toLogin()
					}
				}
			})
		} else {
			// 根据功能导航
			switch(feature) {
				case 'unused':
					uni.switchTab({ url: "../unused/unused" })
					break
				case 'buy':
					uni.switchTab({ url: "../buy/buy" })
					break
			}
		}
	}
	
	const getInfo = async () => {
		try {
			console.log("开始获取用户信息");
			const userId = uni.getStorageSync("userId");
			if (!userId) {
				console.log("未找到用户ID，无法获取用户信息");
				isLoggedIn.value = false;
				return;
			}
			
			isLoggedIn.value = true;
			
			let res = await getInfoApi({
				userId: userId
			});
			
			console.log("获取用户信息响应:", res);
			if (res && res.code == 200 && res.data) {
				console.log("获取用户信息成功:", res.data);
				// 更新用户信息
				nickName.value = res.data.nickName || '用户';
				picture.value = res.data.picture || '';
				userInfo = res.data;
				
				// 保存到本地存储，便于其他页面使用
				uni.setStorageSync('userInfo', res.data);
				
				// 如果有事件总线，发布用户信息更新事件
				if(instance && instance.proxy && instance.proxy.$eventBus) {
					instance.proxy.$eventBus.emit('userInfo-updated', res.data);
				}
			} else {
				console.error("获取用户信息失败或返回数据为空", res);
				// 如果是登录失效，清除登录状态
				if(res && res.code == 600) { // 假设600是未登录状态码
					uni.removeStorageSync('userId');
					isLoggedIn.value = false;
					
					uni.showToast({
						title: '登录已过期，请重新登录',
						icon: 'none'
					});
				}
			}
		} catch (error) {
			console.error("获取用户信息出错:", error);
			uni.showToast({
				title: '获取信息失败，请重新登录',
				icon: 'none'
			});
		}
	}
	
	//修改个人信息
	const toUserInfo = () => {
		if(!userInfo) {
			getInfo().then(() => {
				if(userInfo) {
					uni.navigateTo({
						url: "../user_info/user_info?userInfo=" + JSON.stringify(userInfo)
					});
				}
			});
		} else {
			uni.navigateTo({
				url: "../user_info/user_info?userInfo=" + JSON.stringify(userInfo)
			});
		}
	}
	
	// 更新onShow以检查登录状态
	onShow(() => {
		console.log("个人页面显示，检查登录状态");
		const userId = uni.getStorageSync("userId");
		isLoggedIn.value = !!userId;
		
		if (isLoggedIn.value) {
			// 每次页面显示时，如果已登录，都重新获取用户信息
			getInfo();
		} else {
			// 未登录时重置状态
			nickName.value = '点击登录/注册';
			picture.value = '';
		}
		
		// 尝试从本地存储获取用户信息
		const storedUserInfo = uni.getStorageSync('userInfo');
		if(isLoggedIn.value && storedUserInfo) {
			userInfo = storedUserInfo;
			nickName.value = userInfo.nickName || '用户';
			picture.value = userInfo.picture || '';
		}
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