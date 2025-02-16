package com.itmk.web.goods_category.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itmk.web.goods_category.entity.GoodsCategory;
import com.itmk.web.goods_category.mapper.GoodsCatergoryMapper;
import com.itmk.web.goods_category.service.GoodsCategoryService;
import org.springframework.stereotype.Service;

@Service
public class GoodsCategoryServiceImpl extends ServiceImpl<GoodsCatergoryMapper, GoodsCategory> implements GoodsCategoryService {
}
