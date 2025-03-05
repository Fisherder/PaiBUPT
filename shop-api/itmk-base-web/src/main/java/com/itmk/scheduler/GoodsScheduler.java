package com.itmk.scheduler;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.itmk.web.goods.entity.Goods;
import com.itmk.web.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

        // 查询符合条件的商品列表
        UpdateWrapper<Goods> queryWrapper = new UpdateWrapper<>();
        queryWrapper.lambda()
                .eq(Goods::getStatus, "0")
                .eq(Goods::getSellStatus,"0")// 状态为上架且未出售
                .le(Goods::getCreateTime, overdueTime); // 创建时间早于或等于48小时前

        List<Goods> goodsList = goodsService.list(queryWrapper);

        // 更新商品状态为下架
        for (Goods goods : goodsList) {
            UpdateWrapper<Goods> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda()
                    .set(Goods::getStatus, "1") // 假设"1"代表下架状态
                    .eq(Goods::getGoodsId, goods.getGoodsId());
            goodsService.update(updateWrapper);
        }
    }
}
