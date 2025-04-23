import {
	createSSRApp
} from "vue";
import uView from './uni_modules/vk-uview-ui';
import App from "./App.vue";

export function createApp() {
	const app = createSSRApp(App);

	//使用uview ui
	app.use(uView);

	// 添加Coze配置作为全局属性
	app.config.globalProperties.$cozeConfig = {
		apiKey: 'pat_gP4vPWxbg1JeFUkfz4KmGJc7HASmwcUg3ewhPGHqVhv81ryfqkfGTSM4aGBbb92l',
		botId: '7493773966524989477'
	};

	return {
		app,
	};
}