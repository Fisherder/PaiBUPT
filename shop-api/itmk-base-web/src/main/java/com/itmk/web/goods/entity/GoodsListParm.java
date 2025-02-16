package com.itmk.web.goods.entity;

import lombok.Data;

@Data
public class GoodsListParm {
    //接收列表传递参数
    private Long currentPage;
    private Long pageSize;
    private String goodsName;
}
