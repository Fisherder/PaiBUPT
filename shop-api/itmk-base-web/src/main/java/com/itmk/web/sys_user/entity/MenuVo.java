package com.itmk.web.sys_user.entity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuVo {
    private Long menuId;
    private String title;
    private String path;
    private String icon;
}
