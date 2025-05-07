// 修改 App.vue
<script>
export default {
  globalData: {
    isLoggedIn: false
  },
  onLaunch: function () {
    console.log('App Launch')
    // 检查存储中是否存在userId
    const userId = uni.getStorageSync('userId')
    // 直接修改this上的globalData
    this.globalData.isLoggedIn = !!userId
  },
  onShow: function () {
    console.log('App Show')
  },
  onHide: function () {
    console.log('App Hide')
  }
}
</script>

<style lang="scss">
  page{
    height: 100%;
  }
  @import "./uni_modules/vk-uview-ui/index.scss";
</style>