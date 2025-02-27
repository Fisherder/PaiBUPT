# PaiBUPT - 基于竞拍机制的校园二手交易平台

## 项目背景
在高校校园生态中，二手物品交易始终保持着旺盛的生命力。据统计，全国高校学生群体年均闲置物品交易规模已突破百亿，近83%的大学生参与过二手交易。这种绿色循环的消费模式不仅体现了Z世代践行环保节约的新型消费理念，更成为构建校园经济生态的重要环节。然而传统线下"跳蚤市场"受时空限制明显，现有线上平台普遍存在信息冗杂、沟通低效等问题。特别是在北邮等理工科院校，学生课业压力较大，亟需一个高效便捷且具有校园特色的交易解决方案。

## 项目愿景
PaiBUPT致力于打造专属于大学生的智能二手交易平台，通过竞拍机制重构校园交易场景。我们以北京邮电大学为试点，构建具有强校园属性的闭环交易生态，实现"3分钟快速上架，24小时智能竞拍"的核心目标，让闲置资源在可信的社区环境中高效流通。

## 如何部署
### 项目后端部署
1. 在IDEA中打开后端项目（shop-api）
2. 右键项目->Maven
<img src="https://github.com/user-attachments/assets/ec3a6b21-2b7f-456e-a263-2c00d9e49c87" width=40%>

3. 依次点击“同步项目”“生成源代码并更新文件夹”
4. 运行ShopApplication.java
5. 运行成功结果如图
![image](https://github.com/user-attachments/assets/b5b895d3-e6b5-4eba-bd33-12869a3cd016)
![image](https://github.com/user-attachments/assets/a522dd31-05b8-45af-9e1b-f1c50bd3c06a)


### 项目前端部署
由于前端是基于node.js开发，而git会自动忽略node_modules文件，因此在部署时首先要在shop-pro和shop-pc两个文件夹内分别运行一次命令：
```bash
npm install
```
然后进行如下步骤

#### 小程序前端部署
1. 在HBuilderX中打开前端项目（shop-pro）
2. 双击项目文件夹，选择运行->运行到小程序模拟器->微信开发者工具
![image](https://github.com/user-attachments/assets/3eb85b40-9831-4ba4-9763-15339e9d4e3b)

#### 后台管理页面前端部署
1. 在任意终端中打开前端项目文件夹（shop-pc）
2. 运行命令
```bash
npm run dev
```


## 技术架构
本系统采用前后端分离架构：
前端：Vue.js + ElementUI 构建渐进式Web应用
后端：Spring Boot + MyBatis Plus 微服务架构
数据库：MySQL 8.3 + Redis 6.0 缓存
特色技术：Elasticsearch 商品检索 / 阿里云OSS对象存储 / Docker容器化部署（待实现）
安全框架：JWT鉴权 + Spring Security权限控制（待实现）

## 未来展望
项目仍处在开发初期阶段，欢迎开发者通过Issues参与项目贡献，共同打造更智能的校园交易生态！
