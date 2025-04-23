// pages/coze-chat/coze-chat.vue
<template>
	<view class="container">
		<view class="chat-history" ref="chatContent">
			<view v-for="(item, index) in chatHistory" :key="index" class="message-item"
				:class="item.role === 'user' ? 'user-message' : 'bot-message'">
				<view class="message-content">{{ item.content }}</view>
			</view>
			<view class="loading-area" v-if="isLoading">
				<u-loading mode="circle" size="30"></u-loading>
				<text class="loading-text">AI思考中...</text>
			</view>
		</view>

		<view class="input-area">
			<u-input v-model="inputMessage" placeholder="请输入您的问题..." border="surround" confirmType="send"
				:disabled="isLoading" @confirm="sendMessage" class="message-input"></u-input>
			<u-button type="primary" @click="sendMessage" :loading="isLoading"
				:disabled="isLoading || !inputMessage.trim()" class="send-button">发送</u-button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				inputMessage: '',
				chatHistory: [],
				isLoading: false,
				// Coze API 配置
				cozeConfig: {
					apiKey: 'pat_gP4vPWxbg1JeFUkfz4KmGJc7HASmwcUg3ewhPGHqVhv81ryfqkfGTSM4aGBbb92l', // 您的API密钥
					botId: '7493773966524989477', // 您的Bot ID
					userId: 'user_' + Math.random().toString(36).substring(2, 10) // 生成随机用户ID
				},
				// 当前会话ID
				currentConversationId: null
			}
		},

		onLoad() {
			// 可以在这里添加初始化逻辑，例如显示欢迎消息
			this.chatHistory.push({
				role: 'assistant',
				content: '您好！我是您的AI助手，有什么我可以帮您的吗？建议格式“xxx价格建议为多少，返回具体定价建议和定价数字，不要返回网页和链接和新闻”'
			});
		},

		methods: {
			// 清洗和规范化文本内容
			cleanResponseContent(content) {
				// 检查content是否为字符串
				if (typeof content !== 'string') {
					console.warn('内容不是字符串类型:', content);
					return String(content || '');
				}

				try {
					// 检查是否包含JSON格式数据
					if (content.includes('response_for_model')) {
						// 尝试提取JSON中的实际内容
						const contentRegex = /response_for_model":\["([^"]+)"/;
						const match = content.match(contentRegex);
						if (match && match[1]) {
							content = match[1];
						}
					}

					// 移除HTML标签
					let cleanContent = content.replace(/<\/?[^>]+(>|$)/g, "");

					// 移除URL编码
					cleanContent = cleanContent.replace(/(%[0-9A-F]{2})+/g, " ");

					// 处理转义字符
					cleanContent = cleanContent.replace(/\\n/g, "\n")
						.replace(/\\"/g, '"')
						.replace(/\\t/g, " ")
						.replace(/\\\\/g, "\\");

					// 移除JavaScript代码段
					cleanContent = cleanContent.replace(/\/\/lynxview\/\?[^"]+/g, "");

					// 移除多余的空白字符
					cleanContent = cleanContent.replace(/\s+/g, " ").trim();

					// 移除可能的链接标记
					cleanContent = cleanContent.replace(/\\nlink:http:\/\/[^\s]+/g, "");

					return cleanContent;
				} catch (e) {
					console.error('清洗内容时出错:', e);
					return content; // 出错时返回原始内容
				}
			},

			// 发送消息到Coze Bot
			sendMessage() {
				if (!this.inputMessage.trim()) return;

				// 添加用户消息到聊天记录
				const userMessage = this.inputMessage.trim();
				this.chatHistory.push({
					role: 'user',
					content: userMessage
				});

				// 清空输入框并显示加载状态
				this.inputMessage = '';
				this.isLoading = true;

				// 滚动到底部
				this.$nextTick(() => {
					this.scrollToBottom();
				});

				// 准备请求数据 - 根据v2 API格式调整
				const requestData = {
					bot_id: this.cozeConfig.botId,
					user: this.cozeConfig.userId, // 修改: user_id 改为 user
					query: userMessage, // 添加: 直接使用 query 参数
					stream: false,
					conversation_id: this.currentConversationId || undefined // 如果没有会话ID则不传
				};

				// 控制台输出请求信息（调试用）
				console.log('请求URL: https://api.coze.cn/open_api/v2/chat');
				console.log('请求参数:', JSON.stringify(requestData));

				// 发送请求到Coze API
				uni.request({
					url: 'https://api.coze.cn/open_api/v2/chat', // 使用v2 API端点
					method: 'POST',
					header: {
						'Content-Type': 'application/json',
						'Authorization': `Bearer ${this.cozeConfig.apiKey}`
					},
					data: requestData,
					timeout: 60000, // 增加超时时间到60秒
					success: (res) => {
						console.log('API响应:', res);

						// 检查响应状态
						if (res.statusCode !== 200) {
							this.handleApiError(res);
							return;
						}

						// 处理响应数据 - 适配v2 API响应格式
						this.handleChatResponse(res.data);
					},
					fail: (err) => {
						console.error('请求失败:', err);

						// 显示错误信息
						uni.showToast({
							title: '网络请求失败，请检查网络连接',
							icon: 'none',
							duration: 2000
						});

						this.chatHistory.push({
							role: 'assistant',
							content: '抱歉，我遇到了网络问题，无法回应您的问题。请稍后再试。'
						});
						this.isLoading = false;
					}
				});
			},

			// 处理聊天响应 - 修改为适配v2 API
			handleChatResponse(response) {
				// 保存会话ID
				if (response.conversation_id) {
					this.currentConversationId = response.conversation_id;
					console.log('会话ID:', this.currentConversationId);
				}

				// 查找助手消息
				if (response.messages && response.messages.length > 0) {
					// 获取助手回复
					const assistantMessages = response.messages.filter(
						msg => msg.role === 'assistant' && msg.type === 'answer'
					);

					if (assistantMessages.length > 0) {
						// 获取原始内容并清洗
						const originalContent = assistantMessages[0].content;
						const cleanedContent = this.cleanResponseContent(originalContent);

						console.log('原始内容:', originalContent);
						console.log('清洗后内容:', cleanedContent);

						// 添加到聊天记录
						this.chatHistory.push({
							role: 'assistant',
							content: cleanedContent
						});

						// 滚动到底部
						this.$nextTick(() => {
							this.scrollToBottom();
						});
					} else {
						this.handleEmptyResponse();
					}
				} else {
					this.handleEmptyResponse();
				}

				this.isLoading = false;
			},

			// 处理API错误
			handleApiError(res) {
				console.error('API错误:', res.statusCode, res.data);

				let errorMessage = '抱歉，服务器暂时无法响应，请稍后再试。';

				// 根据状态码提供更具体的错误信息
				if (res.statusCode === 401) {
					errorMessage = '授权验证失败，请检查API密钥是否正确。';
				} else if (res.statusCode === 404) {
					errorMessage = '请求的资源不存在，请检查API地址和Bot ID是否正确。';
				} else if (res.statusCode === 429) {
					errorMessage = '请求过于频繁，请稍后再试。';
				}

				// 显示错误信息
				uni.showToast({
					title: errorMessage,
					icon: 'none',
					duration: 2000
				});

				this.chatHistory.push({
					role: 'assistant',
					content: errorMessage
				});

				this.isLoading = false;
			},

			// 处理空响应
			handleEmptyResponse() {
				console.warn('API响应中没有找到助手消息');

				this.chatHistory.push({
					role: 'assistant',
					content: '抱歉，我没能正确理解您的问题。请您换个方式提问，或者稍后再试。'
				});

				// 滚动到底部
				this.$nextTick(() => {
					this.scrollToBottom();
				});

				this.isLoading = false;
			},

			// 滚动聊天区域到底部
			scrollToBottom() {
				const query = uni.createSelectorQuery().in(this);
				query.select('.chat-history').boundingClientRect();
				query.exec(res => {
					if (res && res[0]) {
						uni.pageScrollTo({
							scrollTop: res[0].height,
							duration: 100
						});
					}
				});
			}
		}
	}
</script>

<style lang="scss">
	.container {
		display: flex;
		flex-direction: column;
		height: 100vh;
		background-color: #f5f5f5;
		padding: 10rpx;
	}

	.chat-history {
		flex: 1;
		overflow-y: auto;
		padding: 20rpx;
		margin-bottom: 20rpx;
	}

	.message-item {
		max-width: 80%;
		margin-bottom: 30rpx;
		padding: 20rpx;
		border-radius: 12rpx;
		word-wrap: break-word;
	}

	.user-message {
		align-self: flex-end;
		margin-left: auto;
		background-color: #007AFF;
		color: white;
	}

	.bot-message {
		align-self: flex-start;
		background-color: white;
		color: #333;
		box-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.1);
	}

	.message-content {
		line-height: 1.5;
		font-size: 30rpx;
	}

	.loading-area {
		display: flex;
		flex-direction: column;
		align-items: center;
		margin: 30rpx 0;
	}

	.loading-text {
		margin-top: 10rpx;
		font-size: 24rpx;
		color: #666;
	}

	.input-area {
		display: flex;
		padding: 20rpx;
		background-color: white;
		border-top: 2rpx solid #eee;
	}

	.message-input {
		flex: 1;
		margin-right: 20rpx;
	}

	.send-button {
		width: 160rpx !important;
		height: 80rpx !important;
		display: flex;
		align-items: center;
		justify-content: center;
	}
</style>