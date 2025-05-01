package com.itmk.web.elasticsearch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itmk.web.elasticsearch.document.GoodsDocument;
import com.itmk.web.elasticsearch.repository.GoodsRepository;
import com.itmk.web.elasticsearch.service.ElasticsearchService;
import com.itmk.web.goods.entity.Goods;
import com.itmk.web.goods.service.GoodsService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Override
    public boolean indexGoods(Goods goods) {
        try {
            GoodsDocument goodsDocument = convertToDocument(goods);
            goodsRepository.save(goodsDocument);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean indexGoodsBatch(List<Goods> goodsList) {
        try {
            List<GoodsDocument> documents = goodsList.stream()
                    .map(this::convertToDocument)
                    .collect(Collectors.toList());
            goodsRepository.saveAll(documents);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteGoods(Long goodsId) {
        try {
            goodsRepository.deleteById(goodsId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateGoods(Goods goods) {
        try {
            GoodsDocument goodsDocument = convertToDocument(goods);
            goodsRepository.save(goodsDocument);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Page<GoodsDocument> searchGoods(String keyword, Long categoryId, Pageable pageable) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 只搜索未删除的商品
        boolQueryBuilder.must(QueryBuilders.termQuery("deleteStatus", "0"));

        // 只搜索已上架的商品
        boolQueryBuilder.must(QueryBuilders.termQuery("status", "0"));

        // 关键词搜索
        if (StringUtils.hasText(keyword)) {
            BoolQueryBuilder keywordQuery = QueryBuilders.boolQuery()
                    .should(QueryBuilders.matchQuery("goodsName", keyword))
                    .should(QueryBuilders.matchQuery("goodsDesc", keyword))
                    .should(QueryBuilders.matchQuery("address", keyword));
            boolQueryBuilder.must(keywordQuery);
        }

        // 分类筛选
        if (categoryId != null && categoryId > 0) {
            boolQueryBuilder.must(QueryBuilders.termQuery("categoryId", categoryId));
        }

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(pageable)
                .build();

        return goodsRepository.search(searchQuery);
    }

    @Override
    public List<GoodsDocument> recommendGoods(Long userId, int limit) {
        // 1. 基于用户历史浏览/购买的商品类别推荐
        List<GoodsDocument> recommendations = new ArrayList<>();

        // 查询用户最近浏览的商品分类（这里简化处理，实际可能需要单独的用户行为表）
        List<Long> recentCategoryIds = new ArrayList<>();

        // 如果没有用户特定的分类，就推荐首页推荐和热门商品
        if (recentCategoryIds.isEmpty()) {
            // 推荐首页推荐的商品
            List<GoodsDocument> indexRecommendations = goodsRepository.findBySetIndexAndStatusAndDeleteStatus("1", "0", "0");
            recommendations.addAll(indexRecommendations);

            // 如果推荐数量不足，添加最新上架的商品
            if (recommendations.size() < limit) {
                int remaining = limit - recommendations.size();

                Pageable pageable = PageRequest.of(0, remaining);
                NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                        .withQuery(QueryBuilders.boolQuery()
                                .must(QueryBuilders.termQuery("status", "0"))
                                .must(QueryBuilders.termQuery("deleteStatus", "0")))
                        .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                        .withPageable(pageable)
                        .build();

                SearchHits<GoodsDocument> searchHits = elasticsearchTemplate.search(searchQuery, GoodsDocument.class);
                List<GoodsDocument> finalRecommendations = recommendations;
                List<GoodsDocument> latestGoods = searchHits.getSearchHits().stream()
                        .map(SearchHit::getContent)
                        .filter(goods -> !finalRecommendations.contains(goods))
                        .collect(Collectors.toList());

                recommendations.addAll(latestGoods);
            }
        } else {
            // 根据用户最近浏览的分类推荐商品
            for (Long categoryId : recentCategoryIds) {
                List<GoodsDocument> categoryGoods = goodsRepository.findByCategoryIdAndDeleteStatus(categoryId, "0");

                // 只添加状态为上架的商品
                categoryGoods = categoryGoods.stream()
                        .filter(goods -> "0".equals(goods.getStatus()))
                        .collect(Collectors.toList());

                recommendations.addAll(categoryGoods);

                if (recommendations.size() >= limit) {
                    break;
                }
            }

            // 如果推荐数量不足，添加首页推荐商品
            if (recommendations.size() < limit) {
                List<GoodsDocument> indexRecommendations = goodsRepository.findBySetIndexAndStatusAndDeleteStatus("1", "0", "0");

                for (GoodsDocument goods : indexRecommendations) {
                    if (!recommendations.contains(goods)) {
                        recommendations.add(goods);

                        if (recommendations.size() >= limit) {
                            break;
                        }
                    }
                }
            }
        }

        // 限制返回数量
        if (recommendations.size() > limit) {
            recommendations = recommendations.subList(0, limit);
        }

        return recommendations;
    }

    @Override
    public boolean syncAllGoodsToEs() {
        try {
            // 查询所有未删除的商品
            QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Goods::getDeleteStatus, "0");
            List<Goods> allGoods = goodsService.list(queryWrapper);

            // 批量索引商品
            return indexGoodsBatch(allGoods);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将商品实体转换为ES文档
     */
    private GoodsDocument convertToDocument(Goods goods) {
        GoodsDocument document = new GoodsDocument();
        BeanUtils.copyProperties(goods, document);
        return document;
    }
}