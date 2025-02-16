package com.itmk.web.goods_category.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("goods_category")
//标明下面的类对应mysql的goods_category
public class GoodsCategory {
    //标明categoryId是主键
    @TableId(type= IdType.AUTO)
    private Long categoryId;
    private String categoryName;
    private Integer orderNum;
}
