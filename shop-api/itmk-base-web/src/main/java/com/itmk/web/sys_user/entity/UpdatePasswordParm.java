package com.itmk.web.sys_user.entity;

import lombok.Data;

@Data
public class UpdatePasswordParm {
    //接收列表传递参数
    private Long userId;
    private String password;
    private String oldPassword;
}
