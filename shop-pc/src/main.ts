import { createApp } from 'vue'
//import './style.css'
import App from './App.vue'
import router from './router/index'
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import myconfirm from './utils/myconfirm'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
//引入pinda构造函数
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
// 实例化 Pinia
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)
const app = createApp(App);
app.use(router).use(ElementPlus,{
    locale: zhCn,
  }).use(pinia).mount('#app')
//全局注册图标组件
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
    }
    //全局挂载
app.config.globalProperties.$myconfirm=myconfirm;