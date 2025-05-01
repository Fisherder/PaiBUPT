package com.itmk.web.elasticsearch.service;

import com.itmk.web.elasticsearch.document.GoodsDocument;
import com.itmk.web.goods.entity.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ElasticsearchService {

    /**
     * 将商品索引到Elasticsearch
     * @param goods 商品对象
     * @return 是否成功
     */
    boolean indexGoods(Goods goods);

    /**
     * 批量索引商品
     * @param goodsList 商品列表
     * @return 是否成功
     */
    boolean indexGoodsBatch(List<Goods> goodsList);

    /**
     * 从Elasticsearch删除商品
     * @param goodsId 商品ID
     * @return 是否成功
     */
    boolean deleteGoods(Long goodsId);

    /**
     * 更新商品索引
     * @param goods 商品对象
     * @return 是否成功
     */
    boolean updateGoods(Goods goods);

    /**
     * 高级搜索商品
     * @param keyword 关键词
     * @param categoryId 分类ID
     * @param pageable 分页对象
     * @return 商品列表
     */
    Page<GoodsDocument> searchGoods(String keyword, Long categoryId, Pageable pageable);

    /**
     * 推荐商品
     * @param userId 用户ID
     * @param limit 推荐数量
     * @return 推荐的商品列表
     */
    List<GoodsDocument> recommendGoods(Long userId, int limit);

    /**
     * 同步所有商品数据到Elasticsearch
     * @return 是否成功
     */
    boolean syncAllGoodsToEs();
}