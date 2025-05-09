package com.itmk.web.sys_banner.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.goods.entity.Goods;
import com.itmk.web.goods.service.GoodsService;
import com.itmk.web.goods_category.entity.SelectType;
import com.itmk.web.sys_banner.entity.BannerParm;
import com.itmk.web.sys_banner.entity.SysBanner;
import com.itmk.web.sys_banner.service.SysBannerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/banner")
@RestController
public class SysBannerController {
    @Autowired
    private SysBannerService sysBannerService;
    @Autowired
    private GoodsService goodsService;
    //新增
    @PostMapping
    public ResultVo add(@RequestBody SysBanner sysBanner) {
        if(sysBannerService.save(sysBanner)) {
            return ResultUtils.success("新增成功！");
        }
        return ResultUtils.error("新增失败！");
    }
    //编辑
    @PutMapping
    public ResultVo edit(@RequestBody SysBanner sysBanner) {
        if(sysBannerService.updateById(sysBanner)) {
            return ResultUtils.success("编辑成功！");
        }
        return ResultUtils.error("编辑失败！");
    }
    //删除
    @DeleteMapping ("/{banId}")
    public ResultVo delete(@PathVariable("banId") Long banId) {
        if(sysBannerService.removeById(banId)) {
            return ResultUtils.success("删除成功！");
        }
        return ResultUtils.error("删除失败！");
    }
    //查询
    @GetMapping("/list")
    public ResultVo list(BannerParm parm) {
        //构造分页对象
        IPage<SysBanner> page=new Page<>(parm.getCurrentPage(), parm.getPageSize());
        //构造查询条件
        QueryWrapper<SysBanner> query=new QueryWrapper();
        query.lambda().like(StringUtils.isNotEmpty(parm.getTitle()),SysBanner::getTitle,parm.getTitle())
                .orderByDesc(SysBanner::getOrderNum);
        IPage<SysBanner> list=sysBannerService.page(page,query);
        return  ResultUtils.success("查询成功！",list);

    }
    //商品列表
    @GetMapping("/getGoods")
    public ResultVo getGoods(){
        //查询物品
        QueryWrapper<Goods> query=new QueryWrapper<>();
        //已上架且未出售的才能使用
        query.lambda().eq(Goods::getStatus,"0").eq(Goods::getSellStatus,"0");
        List<Goods> list=goodsService.list(query);
        //组织前端select需要的数据格式
        List<SelectType> selectList=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            SelectType type=new SelectType();
            type.setValue(list.get(i).getGoodsId());
            type.setLabel(list.get(i).getGoodsName());
            selectList.add(type);
        }
        return ResultUtils.success("查询成功",selectList);
    }

    //上下架
    @PostMapping("/upanddown")
    public ResultVo upanddown(@RequestBody SysBanner sysBanner) {
        UpdateWrapper<SysBanner> query=new UpdateWrapper<>();
        query.lambda().set(SysBanner::getStatus,sysBanner.getStatus())
                .eq(SysBanner::getBanId,sysBanner.getBanId());
        if(sysBannerService.update(query)) {
            return ResultUtils.success("设置成功！");
        }
        return ResultUtils.error("设置失败！");
    }
    // 在 shop-api/itmk-base-web/src/main/java/com/itmk/web/sys_banner/controller/SysBannerController.java 文件中
    @GetMapping("/getIndexBanner")
    public ResultVo getIndexBanner(){
        QueryWrapper<SysBanner> query = new QueryWrapper<>();
        query.lambda().eq(SysBanner::getStatus,"0")  // 只选择状态为0(启用)的轮播图
                .orderByAsc(SysBanner::getOrderNum);
        List<SysBanner> list = sysBannerService.list(query);
        if(list.size() > 0){
            for(int i = 0; i < list.size(); i++){
                Long goodsId = list.get(i).getGoodsId();
                Goods goods = goodsService.getById(goodsId);
                // 只有当商品存在且是上架状态(status为0)且未出售(sellStatus为0)才添加到轮播图中
                if(goods != null && "0".equals(goods.getStatus()) && "0".equals(goods.getSellStatus()) && "0".equals(goods.getDeleteStatus())) {
                    list.get(i).setGoods(goods);
                } else {
                    // 如果商品不存在或已下架或已售出或已删除，则将该轮播图从列表中移除
                    list.remove(i);
                    i--; // 因为移除了一项，索引需要回退一位
                }
            }
        }
        return ResultUtils.success("查询成功", list);
    }

}
