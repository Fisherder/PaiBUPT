package com.itmk.web.user_behavior.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itmk.web.user_behavior.entity.UserBehavior;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserBehaviorMapper extends BaseMapper<UserBehavior> {

    /**
     * 获取用户最近的行为记录
     * @param userId 用户ID
     * @param limit 记录数量限制
     * @return 用户行为记录列表
     */
    @Select("SELECT * FROM user_behavior WHERE user_id = #{userId} ORDER BY create_time DESC LIMIT #{limit}")
    List<UserBehavior> getUserRecentBehaviors(@Param("userId") Long userId, @Param("limit") int limit);

    /**
     * 获取用户浏览过的商品ID列表
     * @param userId 用户ID
     * @param limit 记录数量限制
     * @return 商品ID列表
     */
    @Select("SELECT goods_id FROM user_behavior WHERE user_id = #{userId} AND behavior_type = '1' ORDER BY create_time DESC LIMIT #{limit}")
    List<Long> getUserViewedGoodsIds(@Param("userId") Long userId, @Param("limit") int limit);

    /**
     * 获取用户收藏的商品ID列表
     * @param userId 用户ID
     * @param limit 记录数量限制
     * @return 商品ID列表
     */
    @Select("SELECT goods_id FROM user_behavior WHERE user_id = #{userId} AND behavior_type = '2' ORDER BY create_time DESC LIMIT #{limit}")
    List<Long> getUserFavoriteGoodsIds(@Param("userId") Long userId, @Param("limit") int limit);

    /**
     * 获取用户购买的商品ID列表
     * @param userId 用户ID
     * @param limit 记录数量限制
     * @return 商品ID列表
     */
    @Select("SELECT goods_id FROM user_behavior WHERE user_id = #{userId} AND behavior_type = '3' ORDER BY create_time DESC LIMIT #{limit}")
    List<Long> getUserPurchasedGoodsIds(@Param("userId") Long userId, @Param("limit") int limit);
}