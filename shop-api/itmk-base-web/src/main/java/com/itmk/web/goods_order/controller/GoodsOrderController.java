package com.itmk.web.goods_order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.goods.entity.Goods;
import com.itmk.web.goods.service.GoodsService;
import com.itmk.web.goods_order.entity.GoodsOrder;
import com.itmk.web.goods_order.service.GoodsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("/api/goodsOrder")
public class GoodsOrderController {
    @Autowired
    private GoodsOrderService goodsOrderService;
    @Autowired
    private GoodsService goodsService; // 需要提前注入 GoodsService
    //交易订单
    @PostMapping("/replaceOrder")
    public ResultVo replaceOrder(@RequestBody GoodsOrder order){
        //构造查询条件
        QueryWrapper<Goods> query = new QueryWrapper<>();
        query.lambda().eq(Goods::getGoodsId, order.getGoodsId());
        Goods goods = goodsService.getOne(query); // 获取 Goods 记录
        Date goodsCreateTime = goods.getCreateTime(); // 获取商品的创建时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -48);
        Date deadline = calendar.getTime();
        //当前报价大于先前的且未超48H
        if(goodsCreateTime.after(deadline)&&(goods.getGoodsPrice().compareTo(order.getPrice())<0)){
            goodsOrderService.replaceOrder(order);
            return ResultUtils.success("交易成功!");
        }
       return ResultUtils.error("已经过了拍卖时间或者出价小于先前报价");
    }
    //查询商品信息
    @GetMapping("/getGoodsDetail")
    public ResultVo getGoodsDetail(GoodsOrder order){
        //构造查询条件
        QueryWrapper<Goods> query = new QueryWrapper<>();
        query.lambda().eq(Goods::getGoodsId, order.getGoodsId());
        Goods goods = goodsService.getOne(query); // 获取 Goods 记录
        return ResultUtils.success("查询成功!", goods);

    }
}
