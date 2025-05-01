package com.itmk.web.elasticsearch.utils;

import com.itmk.web.elasticsearch.document.GoodsDocument;
import com.itmk.web.goods.entity.Goods;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 商品推荐工具类
 * 实现基于内容的推荐算法
 */
@Component
public class RecommendationHelper {

    /**
     * 计算两个商品之间的相似度
     * @param goodsA 商品A
     * @param goodsB 商品B
     * @return 相似度分数 (0-1)
     */
    public double calculateSimilarity(GoodsDocument goodsA, GoodsDocument goodsB) {
        double score = 0.0;
        double weight = 0.0;

        // 1. 类别相同加分
        if (goodsA.getCategoryId().equals(goodsB.getCategoryId())) {
            score += 0.3;
            weight += 0.3;
        }

        // 2. 价格相近加分
        if (goodsA.getGoodsPrice() != null && goodsB.getGoodsPrice() != null) {
            double priceA = goodsA.getGoodsPrice().doubleValue();
            double priceB = goodsB.getGoodsPrice().doubleValue();

            // 价格差异在20%以内
            double priceDiff = Math.abs(priceA - priceB) / Math.max(priceA, priceB);
            if (priceDiff <= 0.2) {
                double priceScore = 0.2 * (1 - priceDiff);
                score += priceScore;
                weight += 0.2;
            }
        }

        // 3. 名称相似度
        if (goodsA.getGoodsName() != null && goodsB.getGoodsName() != null) {
            String nameA = goodsA.getGoodsName().toLowerCase();
            String nameB = goodsB.getGoodsName().toLowerCase();

            Set<String> wordsA = new HashSet<>(Arrays.asList(nameA.split("\\s+")));
            Set<String> wordsB = new HashSet<>(Arrays.asList(nameB.split("\\s+")));

            // 计算Jaccard相似度
            Set<String> union = new HashSet<>(wordsA);
            union.addAll(wordsB);

            Set<String> intersection = new HashSet<>(wordsA);
            intersection.retainAll(wordsB);

            double jaccardSim = union.size() > 0 ? (double) intersection.size() / union.size() : 0;
            score += jaccardSim * 0.25;
            weight += 0.25;
        }

        // 4. 描述相似度
        if (goodsA.getGoodsDesc() != null && goodsB.getGoodsDesc() != null) {
            String descA = goodsA.getGoodsDesc().toLowerCase();
            String descB = goodsB.getGoodsDesc().toLowerCase();

            Set<String> wordsA = new HashSet<>(Arrays.asList(descA.split("\\s+")));
            Set<String> wordsB = new HashSet<>(Arrays.asList(descB.split("\\s+")));

            // 计算Jaccard相似度
            Set<String> union = new HashSet<>(wordsA);
            union.addAll(wordsB);

            Set<String> intersection = new HashSet<>(wordsA);
            intersection.retainAll(wordsB);

            double jaccardSim = union.size() > 0 ? (double) intersection.size() / union.size() : 0;
            score += jaccardSim * 0.25;
            weight += 0.25;
        }

        // 归一化得分
        return weight > 0 ? score / weight : 0;
    }

    /**
     * 基于给定的商品列表，获取相似商品推荐
     * @param referenceGoods 参考商品列表（用户浏览/交互过的商品）
     * @param candidateGoods 候选商品列表
     * @param limit 推荐数量
     * @return 推荐商品列表
     */
    public List<GoodsDocument> getContentBasedRecommendations(
            List<GoodsDocument> referenceGoods,
            List<GoodsDocument> candidateGoods,
            int limit) {
        // 记录每个候选商品的最高相似度分数
        Map<Long, Double> goodsScores = new HashMap<>();

        // 计算相似度
        for (GoodsDocument reference : referenceGoods) {
            for (GoodsDocument candidate : candidateGoods) {
                // 跳过已浏览的商品
                if (reference.getGoodsId().equals(candidate.getGoodsId())) {
                    continue;
                }

                double similarity = calculateSimilarity(reference, candidate);

                // 更新最高相似度
                goodsScores.put(candidate.getGoodsId(),
                        Math.max(similarity, goodsScores.getOrDefault(candidate.getGoodsId(), 0.0)));
            }
        }

        // 根据相似度排序
        List<Map.Entry<Long, Double>> sortedScores = new ArrayList<>(goodsScores.entrySet());
        sortedScores.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // 获取前N个推荐结果
        List<GoodsDocument> recommendations = new ArrayList<>();
        for (int i = 0; i < Math.min(limit, sortedScores.size()); i++) {
            Long goodsId = sortedScores.get(i).getKey();

            // 查找对应商品对象
            for (GoodsDocument goods : candidateGoods) {
                if (goods.getGoodsId().equals(goodsId)) {
                    recommendations.add(goods);
                    break;
                }
            }
        }

        return recommendations;
    }
}