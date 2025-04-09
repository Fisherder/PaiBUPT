package com.itmk.web.wx_user.entity;

import lombok.Data;
@Data
public class UpdateParm {

        private Long userId;
        private String password;
        private String oldPassword;

}
