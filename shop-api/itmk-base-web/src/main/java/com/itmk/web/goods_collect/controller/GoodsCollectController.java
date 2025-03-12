package com.itmk.web.goods_collect.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.goods_collect.entity.GoodsCollect;
import com.itmk.web.goods_collect.service.GoodsCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RequestMapping("/api/collect")
@RestController
public class GoodsCollectController {
    @Autowired
    private GoodsCollectService goodsCollectService;
    //收藏
    @PostMapping("/collect")
    public ResultVo collect(@RequestBody GoodsCollect goodsCollect){
//判断是否已经收藏
        QueryWrapper<GoodsCollect> query = new QueryWrapper<>();
        query.lambda().eq(GoodsCollect::getGoodsId,goodsCollect.getGoodsId())
                .eq(GoodsCollect::getUserId,goodsCollect.getUserId());
        GoodsCollect one = goodsCollectService.getOne(query);
        if(one == null){ //未收藏
            goodsCollect.setCollectTime(new Date());
            if(goodsCollectService.save(goodsCollect)){
                return ResultUtils.success("收藏成功!");
            }
            return ResultUtils.error("收藏失败!");
        }else{ //已收藏,取消收藏
            if(goodsCollectService.remove(query)){
                return ResultUtils.success("取消收藏!");
            }
            return ResultUtils.error("取消失败!");
        }
    }
    //判断是否已经收藏
    @GetMapping("/hasCollect")
    public ResultVo hasCollect(GoodsCollect goodsCollect){
//判断是否已经收藏
        QueryWrapper<GoodsCollect> query = new QueryWrapper<>();
        query.lambda().eq(GoodsCollect::getGoodsId,goodsCollect.getGoodsId())
                .eq(GoodsCollect::getUserId,goodsCollect.getUserId());
        GoodsCollect one = goodsCollectService.getOne(query);
        if(one != null){ //已经收藏
            return ResultUtils.success("查询成功","1");
        }else{ //未收藏
            return ResultUtils.success("查询成功","0");
        }
    }
}