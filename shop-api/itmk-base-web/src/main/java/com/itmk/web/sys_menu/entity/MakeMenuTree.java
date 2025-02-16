package com.itmk.web.sys_menu.entity;
import org.springframework.beans.BeanUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MakeMenuTree {
    //pid是确认当前的父节点
    public static List<SysMenu> makeTree(List<SysMenu> menuList, Long pid) {
//接收组装后的树数据
        List<SysMenu> list = new ArrayList<>();
//判断menuList是否为空，如果为空，调用orelse,直接返回空的数据，不为空走stream过滤条件走forEach
        Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> item != null && item.getParentId().equals(pid))
                .forEach(item -> {
//组装树数据
                    SysMenu menu = new SysMenu();
//快速复制值
                    BeanUtils.copyProperties(item, menu);
//设置,树形数据需要label和value
                    menu.setLabel(item.getTitle());
                    menu.setValue(item.getMenuId());
//递归查找下级children,自己调用自己
                    List<SysMenu> children = makeTree(menuList,
                            item.getMenuId());
                    menu.setChildren(children);
                    list.add(menu);
                });
        return list;
    }
}