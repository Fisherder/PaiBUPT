package com.itmk.web.sys_user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data//自动生成get和set方法
@TableName("sys_user")//标明SysUser和sys_user这个表绑定
public class SysUser {
    @TableId(type= IdType.AUTO)//标明userId是主键且为自动递增
    private Long userId;//库中带下划线的比如user_id去下划线后面一个字母变为大写
    private String username;
    private String password;
    private String nickName;
    private String phone;
    private String sex;
    private String status;
    private String isAdmin;
}
