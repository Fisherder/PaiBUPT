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
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		onLoad,
		onReady
	} from '@dcloudio/uni-app';
	import {
		ref
	} from 'vue';
	import {
		collectApi,
		hasCollectApi
	} from '../../api/unused';
	// 导入地图服务
	import mapService from '../../utils/map-service';
	// 地图相关
	const latitude = ref(null);  // 卖家位置纬度
	const longitude = ref(null); // 卖家位置经度
	const userLatitude = ref(null);  // 用户位置纬度
	const userLongitude = ref(null); // 用户位置经度
	const markers = ref([]); // 地图标记点
	const polyline = ref([]); // 路线
	const isLoadingMap = ref(false); // 地图加载状态
	const distanceText = ref(''); // 距离文本
	const mapScale = ref(14); // 地图缩放级别
	const includePoints = ref([]); // 地图包含点
	const mapContext = ref(null); // 地图上下文
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
	const goodsId = ref('')
	//跳转首页
	const toHome = () => {
		uni.switchTab({
			url: "../index/index"
		})
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
	//收藏按钮
	const collectBtn = async () => {
		const userId = uni.getStorageSync("userId")
		if (!userId) {
			uni.showModal({
				title: '提示',
				content: '收藏功能需要登录，是否前往登录？',
				success: function(res) {
					if (res.confirm) {
						uni.navigateTo({
							url: '../login/login'
						})
					}
				}
			})
			return
		}
		let res = await collectApi({
			userId: uni.getStorageSync("userId"),
			goodsId: goodsId.value
		})
		if (res && res.code == 200) {
			console.log(res)
			hasCollect()
		}
	}
	// 初始化地图数据
	const initMapData = async () => {
		if (!address.value) return;

		isLoadingMap.value = true;

		try {
			// 地理编码 - 将地址转换为经纬度
			const result = await mapService.geocoder(address.value);

			latitude.value = result.latitude;
			longitude.value = result.longitude;

			// 获取当前用户位置
			try {
				const currentLocation = await mapService.getCurrentLocation();
				userLatitude.value = currentLocation.latitude;
				userLongitude.value = currentLocation.longitude;

				// 计算距离
				const distance = await mapService.calculateDistance(
					currentLocation.latitude,
					currentLocation.longitude,
					result.latitude,
					result.longitude
				);

				// 格式化距离显示
				distanceText.value =
					`距离您: ${mapService.formatDistance ? mapService.formatDistance(distance) : distance + '米'}`;

				// 设置包含点 - 确保地图可以显示两个点
				includePoints.value = [{
						latitude: result.latitude,
						longitude: result.longitude
					},
					{
						latitude: currentLocation.latitude,
						longitude: currentLocation.longitude
					}
				];

				// 计算适合的缩放级别
				if (mapService.calculateZoomLevel) {
					mapScale.value = mapService.calculateZoomLevel(
						result.latitude,
						result.longitude,
						currentLocation.latitude,
						currentLocation.longitude
					);
				} else {
					// 如果没有计算缩放级别的函数，使用默认值
					const distance = Math.sqrt(
						Math.pow(result.latitude - currentLocation.latitude, 2) +
						Math.pow(result.longitude - currentLocation.longitude, 2)
					);
					// 简单的缩放级别计算
					if (distance > 0.1) mapScale.value = 10;
					else if (distance > 0.05) mapScale.value = 12;
					else if (distance > 0.01) mapScale.value = 14;
					else if (distance > 0.005) mapScale.value = 15;
					else mapScale.value = 16;
				}

				// 设置标记点
				markers.value = [
					// 卖家位置标记
					{
						id: 1,
						latitude: result.latitude,
						longitude: result.longitude,
						title: goodsName.value,
						callout: {
							content: address.value,
							color: '#000000',
							fontSize: 14,
							borderRadius: 4,
							padding: 8,
							display: 'ALWAYS',
							bgColor: '#ffffff'
						},
						iconPath: '/static/seller_marker.jpg', // 卖家标记图标
						width: 30,
						height: 30
					},
					// 用户位置标记
					{
						id: 2,
						latitude: currentLocation.latitude,
						longitude: currentLocation.longitude,
						title: '我的位置',
						callout: {
							content: '我的位置',
							color: '#000000',
							fontSize: 14,
							borderRadius: 4,
							padding: 8,
							display: 'ALWAYS',
							bgColor: '#ffffff'
						},
						iconPath: '/static/user_marker.jpeg', // 用户标记图标
						width: 30,
						height: 30
					}
				];

				// 获取路线规划
				try {
					if (mapService.getDirections) {
						const directions = await mapService.getDirections(
							currentLocation.latitude,
							currentLocation.longitude,
							result.latitude,
							result.longitude
						);

						if (directions && directions.routes && directions.routes.length > 0) {
							// 解析路线
							const points = mapService.parsePolyline ?
								mapService.parsePolyline(directions.routes[0].polyline) : [{
										latitude: currentLocation.latitude,
										longitude: currentLocation.longitude
									},
									{
										latitude: result.latitude,
										longitude: result.longitude
									}
								];

							// 设置路线
							polyline.value = [{
								points: points,
								color: '#1aad19',
								width: 4,
								dottedLine: false,
								arrowLine: true
							}];

							// 更新路线信息
							const routeDuration = directions.routes[0].duration;
							const formattedDuration = routeDuration > 3600 ?
								`${Math.floor(routeDuration / 3600)}小时${Math.floor((routeDuration % 3600) / 60)}分钟` :
								`${Math.floor(routeDuration / 60)}分钟`;

							distanceText.value =
								`距离您: ${mapService.formatDistance ? mapService.formatDistance(distance) : distance + '米'} (驾车约${formattedDuration})`;
						}
					} else {
						// 如果没有路线规划函数，使用简单的直线
						polyline.value = [{
							points: [{
									latitude: currentLocation.latitude,
									longitude: currentLocation.longitude
								},
								{
									latitude: result.latitude,
									longitude: result.longitude
								}
							],
							color: '#1aad19',
							width: 4,
							dottedLine: false,
							arrowLine: true
						}];
					}
				} catch (error) {
					console.error('获取路线失败:', error);
					// 如果路线规划失败，使用简单的直线
					polyline.value = [{
						points: [{
								latitude: currentLocation.latitude,
								longitude: currentLocation.longitude
							},
							{
								latitude: result.latitude,
								longitude: result.longitude
							}
						],
						color: '#1aad19',
						width: 4,
						dottedLine: false,
						arrowLine: true
					}];
				}
			} catch (err) {
				console.error('获取用户位置失败:', err);
				// 如果获取用户位置失败，只显示卖家位置
				markers.value = [{
					id: 1,
					latitude: result.latitude,
					longitude: result.longitude,
					title: goodsName.value,
					callout: {
						content: address.value,
						color: '#000000',
						fontSize: 14,
						borderRadius: 4,
						padding: 8,
						display: 'ALWAYS',
						bgColor: '#ffffff'
					},
					iconPath: '/static/seller_marker.png',
					width: 30,
					height: 30
				}];
			}

			console.log('地图初始化成功');
		} catch (error) {
			console.error('初始化地图数据失败:', error);
			uni.showToast({
				title: '地图加载失败',
				icon: 'none'
			});
		} finally {
			isLoadingMap.value = false;
		}
	};

	// 打开地图导航
	const openNavigation = () => {
		if (!latitude.value || !longitude.value) return;

		uni.openLocation({
			latitude: latitude.value,
			longitude: longitude.value,
			name: goodsName.value,
			address: address.value,
			scale: 18,
			success: function() {
				console.log('打开导航成功');
			},
			fail: function(err) {
				console.error('打开导航失败', err);
				uni.showToast({
					title: '打开导航失败',
					icon: 'none'
				});
			}
		});
	};

	// 切换地图视图（切换到用户位置、卖家位置或两者都显示）
	const switchMapView = (type) => {
		if (!mapContext.value) return;

		if (type === 'seller' && latitude.value && longitude.value) {
			// 显示卖家位置
			mapContext.value.moveToLocation({
				latitude: latitude.value,
				longitude: longitude.value,
				success: () => {
					mapScale.value = 16;
				}
			});
		} else if (type === 'user' && userLatitude.value && userLongitude.value) {
			// 显示用户位置
			mapContext.value.moveToLocation({
				latitude: userLatitude.value,
				longitude: userLongitude.value,
				success: () => {
					mapScale.value = 16;
				}
			});
		} else if (type === 'both' && includePoints.value.length > 0) {
			// 显示两者
			mapContext.value.includePoints({
				points: includePoints.value,
				padding: [50, 50, 50, 50]
			});
		}
	};
	// 在onReady中获取地图上下文
	onReady(() => {
		mapContext.value = uni.createMapContext('itemMap');
	});
onLoad((options) => {
		const goods = JSON.parse(options.goods)
		console.log(goods)
		goodsId.value = goods.goodsId;
		//物品图片：轮播图数据
		if (goods.image) {
			swipperList.value = goods.image.split(",");
		}
		goodsName.value = goods.goodsName;
		goodsDesc.value = goods.goodsDesc;
		address.value = goods.address;
		goodsPrice.value = goods.goodsPrice
		createTime.value = goods.createTime
		phone.value = goods.phone
		wxNum.value = goods.wxNum
		//查询是否收藏
		hasCollect();
		// 初始化地图
		initMapData();
	})
</script>

<style lang="scss">
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
		margin-bottom: 80px;
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