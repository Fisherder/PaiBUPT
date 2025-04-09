package com.itmk.web.goods.entity;
import lombok.Data;

import java.util.Date;

@Data
public class UpandDownParm {
    private Long goodsId;
    private String status;
    private String setIndex;
    private Date createTime;
}
