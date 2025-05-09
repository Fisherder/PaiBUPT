package com.itmk.web.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itmk.web.goods.entity.Goods;
import org.apache.ibatis.annotations.Param;

public interface GoodsMapper extends BaseMapper<Goods> {
    //查询我的订单
    IPage<Goods> getMyOrder(IPage<Goods> page, @Param("userId") Long userId);
    //查询出售订单
    IPage<Goods> getSellOrder(IPage<Goods> page, @Param("userId") Long userId);
}
