package com.itmk.web.elasticsearch.controller;

import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.elasticsearch.document.GoodsDocument;
import com.itmk.web.elasticsearch.service.ElasticsearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/es")
@Api(tags = "商品搜索和推荐接口")
public class ElasticsearchController {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @GetMapping("/sync")
    @ApiOperation("同步所有商品数据到Elasticsearch")
    public ResultVo syncAllGoodsToEs() {
        if (elasticsearchService.syncAllGoodsToEs()) {
            return ResultUtils.success("同步成功！");
        }
        return ResultUtils.error("同步失败！");
    }

    @GetMapping("/search")
    @ApiOperation("搜索商品")
    public ResultVo searchGoods(
            @ApiParam("搜索关键词") @RequestParam(required = false) String keyword,
            @ApiParam("分类ID") @RequestParam(required = false) Long categoryId,
            @ApiParam("页码") @RequestParam(defaultValue = "1") int page,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") int size) {

        // 页码从0开始
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<GoodsDocument> result = elasticsearchService.searchGoods(keyword, categoryId, pageable);

        return ResultUtils.success("查询成功", result);
    }

    @GetMapping("/recommend")
    @ApiOperation("推荐商品")
    public ResultVo recommendGoods(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("推荐数量") @RequestParam(defaultValue = "6") int limit) {

        List<GoodsDocument> recommendations = elasticsearchService.recommendGoods(userId, limit);

        return ResultUtils.success("推荐成功", recommendations);
    }

    @GetMapping("/hot")
    @ApiOperation("热门商品")
    public ResultVo getHotGoods(
            @ApiParam("数量限制") @RequestParam(defaultValue = "6") int limit) {

        // 热门商品目前简单实现为推荐到首页的商品
        List<GoodsDocument> hotGoods = elasticsearchService.recommendGoods(null, limit);

        return ResultUtils.success("查询成功", hotGoods);
    }
}