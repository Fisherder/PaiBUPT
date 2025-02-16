package com.itmk.web.sys_user.entity;
import lombok.Data;

@Data
public class PageParm {
    //接收列表传递参数
    private Long currentPage;
    private Long pageSize;
    private String nickName;
}