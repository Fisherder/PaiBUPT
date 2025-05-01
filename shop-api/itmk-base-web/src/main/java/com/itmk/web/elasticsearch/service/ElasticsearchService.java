package com.itmk.web.elasticsearch.service;

import com.itmk.web.elasticsearch.document.GoodsDocument;
import com.itmk.web.goods.entity.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ElasticsearchService {

    boolean indexGoods(Goods goods);

    boolean indexGoodsBatch(List<Goods> goodsList);

    boolean deleteGoods(Long goodsId);

    boolean updateGoods(Goods goods);

    Page<GoodsDocument> searchGoods(String keyword, Long categoryId, Pageable pageable);

    List<GoodsDocument> recommendGoods(String setIndex, String status, String deleteStatus);

    List<GoodsDocument> recommendGoods(Long userId, int limit);

    boolean syncAllGoodsToEs();

    // 新增方法：删除并重建索引
    boolean deleteAndRecreateIndex();
}