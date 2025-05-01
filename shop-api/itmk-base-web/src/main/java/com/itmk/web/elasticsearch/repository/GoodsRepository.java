package com.itmk.web.elasticsearch.repository;

import com.itmk.web.elasticsearch.document.GoodsDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsRepository extends ElasticsearchRepository<GoodsDocument, Long> {

    // 根据商品名称查询
    List<GoodsDocument> findByGoodsNameLike(String goodsName);

    // 根据分类ID查询
    List<GoodsDocument> findByCategoryIdAndDeleteStatus(Long categoryId, String deleteStatus);

    // 查询推荐到首页的商品
    List<GoodsDocument> findBySetIndexAndStatusAndDeleteStatus(String setIndex, String status, String deleteStatus);

    // 根据描述查询
    List<GoodsDocument> findByGoodsDescLike(String goodsDesc);

    // 根据地址查询
    List<GoodsDocument> findByAddressLike(String address);
}