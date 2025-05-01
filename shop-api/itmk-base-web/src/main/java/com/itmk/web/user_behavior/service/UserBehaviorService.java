package com.itmk.web.user_behavior.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itmk.web.user_behavior.entity.UserBehavior;

import java.util.List;

public interface UserBehaviorService extends IService<UserBehavior> {

    /**
     * 记录用户浏览商品行为
     * @param userId 用户ID
     * @param goodsId 商品ID
     * @return 是否成功
     */
    boolean recordView(Long userId, Long goodsId);

    /**
     * 记录用户收藏商品行为
     * @param userId 用户ID
     * @param goodsId 商品ID
     * @return 是否成功
     */
    boolean recordFavorite(Long userId, Long goodsId);

    /**
     * 记录用户购买商品行为
     * @param userId 用户ID
     * @param goodsId 商品ID
     * @return 是否成功
     */
    boolean recordPurchase(Long userId, Long goodsId);

    /**
     * 获取用户最近浏览的商品ID列表
     * @param userId 用户ID
     * @param limit 数量限制
     * @return 商品ID列表
     */
    List<Long> getUserViewedGoodsIds(Long userId, int limit);

    /**
     * 获取用户收藏的商品ID列表
     * @param userId 用户ID
     * @param limit 数量限制
     * @return 商品ID列表
     */
    List<Long> getUserFavoriteGoodsIds(Long userId, int limit);

    /**
     * 获取用户购买的商品ID列表
     * @param userId 用户ID
     * @param limit 数量限制
     * @return 商品ID列表
     */
    List<Long> getUserPurchasedGoodsIds(Long userId, int limit);

    /**
     * 获取用户最近的行为记录
     * @param userId 用户ID
     * @param limit 数量限制
     * @return 用户行为记录列表
     */
    List<UserBehavior> getUserRecentBehaviors(Long userId, int limit);
}