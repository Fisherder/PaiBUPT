package com.itmk.web.user_behavior.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itmk.web.user_behavior.entity.UserBehavior;
import com.itmk.web.user_behavior.mapper.UserBehaviorMapper;
import com.itmk.web.user_behavior.service.UserBehaviorService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserBehaviorServiceImpl extends ServiceImpl<UserBehaviorMapper, UserBehavior> implements UserBehaviorService {

    @Override
    public boolean recordView(Long userId, Long goodsId) {
        return recordBehavior(userId, goodsId, "1");
    }

    @Override
    public boolean recordFavorite(Long userId, Long goodsId) {
        return recordBehavior(userId, goodsId, "2");
    }

    @Override
    public boolean recordPurchase(Long userId, Long goodsId) {
        return recordBehavior(userId, goodsId, "3");
    }

    /**
     * 记录用户行为的通用方法
     * @param userId 用户ID
     * @param goodsId 商品ID
     * @param behaviorType 行为类型：1-浏览，2-收藏，3-购买
     * @return 是否成功
     */
    private boolean recordBehavior(Long userId, Long goodsId, String behaviorType) {
        try {
            // 检查是否已存在相同记录
            QueryWrapper<UserBehavior> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(UserBehavior::getUserId, userId)
                    .eq(UserBehavior::getGoodsId, goodsId)
                    .eq(UserBehavior::getBehaviorType, behaviorType);

            UserBehavior existBehavior = this.getOne(queryWrapper);

            if (existBehavior != null) {
                // 已存在记录，更新时间即可
                existBehavior.setCreateTime(new Date());
                return this.updateById(existBehavior);
            } else {
                // 创建新记录
                UserBehavior behavior = new UserBehavior();
                behavior.setUserId(userId);
                behavior.setGoodsId(goodsId);
                behavior.setBehaviorType(behaviorType);
                behavior.setCreateTime(new Date());
                return this.save(behavior);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Long> getUserViewedGoodsIds(Long userId, int limit) {
        return this.baseMapper.getUserViewedGoodsIds(userId, limit);
    }

    @Override
    public List<Long> getUserFavoriteGoodsIds(Long userId, int limit) {
        return this.baseMapper.getUserFavoriteGoodsIds(userId, limit);
    }

    @Override
    public List<Long> getUserPurchasedGoodsIds(Long userId, int limit) {
        return this.baseMapper.getUserPurchasedGoodsIds(userId, limit);
    }

    @Override
    public List<UserBehavior> getUserRecentBehaviors(Long userId, int limit) {
        return this.baseMapper.getUserRecentBehaviors(userId, limit);
    }
}