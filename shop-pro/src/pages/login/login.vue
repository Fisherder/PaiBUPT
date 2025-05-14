<template>
	<view class="u-p-t-40 u-p-r-60 u-p-b-30 u-p-l-60 logincontainer">
		<u-avatar size="140" src=pic mode="circle"></u-avatar>
		<u-form class="forms" :model="loginModel" ref="form1">
			<u-form-item left-icon="account-fill" left-icon-style="font-size:24px;color:#FF7670;"><u-input
					placeholder="请输入账户" v-model="loginModel.username" /></u-form-item>
			<u-form-item left-icon="lock" left-icon-style="font-size:24px;color:#FF7670;">
				<u-input type="password" placeholder="请输入密码" v-model="loginModel.password" /></u-form-item>
			<view @click="toForget" class="passtext">
				忘记密码
			</view>
			<u-button @click="toCommit" :custom-style="customStyle1">登录</u-button>
			<u-button @click="toRegister" type="success" :custom-style="customStyle2">注册</u-button>
		</u-form>

	</view>
</template>

<script setup>
	import {
		reactive,
		ref,
		getCurrentInstance
	} from 'vue';
	import {
		loginApi
	} from '../../api/user';
	const pic = ref('/static/user.jpg')
	const loginModel = reactive({
		username: '',
		password: ''
	})
	//登录
	// login.vue 页面中 toCommit 函数的改进版本

	const toCommit = async () => {
		if (!loginModel.username) {
			uni.showToast({
				title: '请输入账户',
				icon: 'none',
				duration: 2000
			})
			return;
		}
		if (!loginModel.password) {
			uni.showToast({
				title: '请输入密码',
				icon: 'none',
				duration: 2000
			})
			return;
		}

		try {
			let res = await loginApi(loginModel)
			if (res && res.code == 200) {
				// 保存用户ID到本地存储
				console.log("登录成功，保存用户信息:", res.data);
				uni.setStorageSync('userId', res.data.userId);

				// 使用全局方法处理登录成功
				const instance = getCurrentInstance();
				if (instance && instance.proxy && instance.proxy.$appData) {
					// 保存用户信息并触发登录成功事件
					instance.proxy.$appData.loginSuccess(res.data);
				}

				// 检查是否有登录后重定向
				const redirectInfo = uni.getStorageSync('redirect_after_login');
				if (redirectInfo) {
					uni.removeStorageSync('redirect_after_login'); // 清除重定向信息

					console.log("检测到重定向信息，准备跳转到:", redirectInfo);

					if (redirectInfo.page === 'unused_detail' && redirectInfo.params?.goodsId) {
						// 获取商品详情
						let goodsDetail = await getGoodsDetailApi({
							goodsId: redirectInfo.params.goodsId
						});

						if (goodsDetail && goodsDetail.code === 200 && goodsDetail.data) {
							// 重定向到商品详情页
							uni.redirectTo({
								url: `../unused_detail/unused_detail?goods=${encodeURIComponent(JSON.stringify(goodsDetail.data))}`
							});
							return;
						}
					}
				}

				// 没有重定向信息或获取商品详情失败，则使用默认重定向逻辑
				const pages = getCurrentPages();
				if (pages.length > 1) {
					uni.navigateBack();
				} else {
					uni.switchTab({
						url: '../index/index'
					});
				}
			} else {
				uni.showToast({
					title: res?.msg || '登录失败',
					icon: 'none',
					duration: 2000
				});
			}
		} catch (error) {
			console.error("登录过程出错:", error);
			uni.showToast({
				title: '登录过程中出现错误',
				icon: 'none',
				duration: 2000
			});
		}
	}
	//去注册
	const toRegister = () => {
		uni.navigateTo({
			url: '../register/register'
		})
	}
	//忘记密码
	const toForget = () => {
		uni.navigateTo({
			url: "../forget_password/forget_password"
		})
	}
	const customStyle1 = reactive({
		marginTop: '50px',
		background: '#FF7670',
		color: '#FFF',
		width: '100%'
	})
	const customStyle2 = reactive({
		marginTop: '20px',
		// background:'#FF7670',
		color: '#FFF',
		width: '100%'
	})
</script>

<style lang="scss">
	.logincontainer {
		height: 100%;
		display: flex;
		align-items: center;
		flex-direction: column;
	}

	.forms {
		width: 100%;
	}

	.passtext {
		display: flex;
		justify-content: flex-end;
		color: #FF7670;
		margin-top: 15px;
	}
</style>