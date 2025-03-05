package com.itmk.web.goods_order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itmk.web.goods.entity.Goods;
import com.itmk.web.goods_order.entity.GoodsOrder;

public interface GoodsOrderService extends IService<GoodsOrder> {

    void replaceOrder(GoodsOrder goodsOrder);


}
