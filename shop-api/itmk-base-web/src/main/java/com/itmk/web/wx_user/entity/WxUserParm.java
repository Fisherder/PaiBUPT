package com.itmk.web.wx_user.entity;

import lombok.Data;

@Data
public class WxUserParm {
    //接收列表传递参数
    private Long currentPage;
    private Long pageSize;
    private String phone;//根据电话查询
}
