// components/test1/test1.js
Component({

  /**
   * 组件的属性列表
   */
  properties: {

  },

  /**
   * 组件的初始数据
   */
  data: {
    a : 0,
    b : 0,
    sum : 0
  },

  /**
   * 组件的方法列表
   */
  methods: {
    addA(){
      this.setData({a : this.data.a + 1 })
    },
    addB(){
      this.setData({b : this.data.b + 1})
    }
  },
  observers:{
    "a,b" : function(newN1,newN2){
        this.setData({
          sum : newN1 + newN2
        })
    }
  }
})