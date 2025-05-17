package com.itmk.scheduler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.itmk.web.goods.entity.Goods;
import com.itmk.web.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class GoodsScheduler {
    @Autowired
    private GoodsService goodsService;

    /**
     * 定时任务：每24小时检查一次，下架超过72小时未售出的商品
     */
    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // 每24小时执行一次
    public void checkAndUnshelveOverdueGoods() {
        Date currentTime = new Date();
        long currentMillis = currentTime.getTime();

        // 计算72小时前的时间
        long overdueTimeMillis = currentMillis - (72L * 60 * 60 * 1000);
        Date overdueTime = new Date(overdueTimeMillis);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("当前时间: " + sdf.format(currentTime));
        System.out.println("过期时间: " + sdf.format(overdueTime));

        // 查询符合条件的商品列表
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Goods::getStatus, "0")
                .eq(Goods::getSellStatus, "0") // 状态为上架且未出售
                .lt(Goods::getCreateTime, overdueTime); // 创建时间早于72小时前

        List<Goods> goodsList = goodsService.list(queryWrapper);

        System.out.println("找到 " + goodsList.size() + " 个需要下架的商品");

        // 更新商品状态为下架
        for (Goods goods : goodsList) {
            UpdateWrapper<Goods> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda()
                    .set(Goods::getStatus, "1") // "1"代表下架状态
                    .eq(Goods::getGoodsId, goods.getGoodsId());

            boolean success = goodsService.update(updateWrapper);
            System.out.println("下架商品ID: " + goods.getGoodsId() + ", 商品名称: " +
                    goods.getGoodsName() + ", 创建时间: " + sdf.format(goods.getCreateTime()) +
                    ", 下架" + (success ? "成功" : "失败"));
        }
    }
}