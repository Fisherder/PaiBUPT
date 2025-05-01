package com.itmk.web.user_behavior.controller;

import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.user_behavior.service.UserBehaviorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/behavior")
@Api(tags = "用户行为接口")
public class UserBehaviorController {

    @Autowired
    private UserBehaviorService userBehaviorService;

    @PostMapping("/view")
    @ApiOperation("记录用户浏览商品")
    public ResultVo recordView(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("商品ID") @RequestParam Long goodsId) {

        if (userBehaviorService.recordView(userId, goodsId)) {
            return ResultUtils.success("记录成功");
        }
        return ResultUtils.error("记录失败");
    }

    @PostMapping("/favorite")
    @ApiOperation("记录用户收藏商品")
    public ResultVo recordFavorite(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("商品ID") @RequestParam Long goodsId) {

        if (userBehaviorService.recordFavorite(userId, goodsId)) {
            return ResultUtils.success("收藏成功");
        }
        return ResultUtils.error("收藏失败");
    }

    @PostMapping("/purchase")
    @ApiOperation("记录用户购买商品")
    public ResultVo recordPurchase(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("商品ID") @RequestParam Long goodsId) {

        if (userBehaviorService.recordPurchase(userId, goodsId)) {
            return ResultUtils.success("记录成功");
        }
        return ResultUtils.error("记录失败");
    }
}