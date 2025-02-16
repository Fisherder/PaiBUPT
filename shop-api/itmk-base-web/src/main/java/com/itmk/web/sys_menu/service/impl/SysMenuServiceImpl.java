package com.itmk.web.sys_menu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itmk.web.sys_menu.entity.MakeMenuTree;
import com.itmk.web.sys_menu.entity.SysMenu;
import com.itmk.web.sys_menu.mapper.SysMenuMapper;
import com.itmk.web.sys_menu.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Override
    //注入
    public List<SysMenu> getParent(){
        //查询出所有的菜单
        QueryWrapper<SysMenu> query=new QueryWrapper<>();
        //为1是菜单，为0是按钮
        query.lambda().eq(SysMenu::getType,"1")
                .orderByAsc(SysMenu::getOrderNum);
        List<SysMenu> menuList=this.baseMapper.selectList(query);
        //构造顶级菜单
        SysMenu menu=new SysMenu();
        menu.setMenuId(0L);
        menu.setParentId(-1L);
        menu.setTitle("顶级菜单");
        menu.setLabel("顶级菜单");
        menu.setValue(0L);
        //添加到菜单数据
        menuList.add(menu);
        //组装树形数据,-1是pid父节点
        return MakeMenuTree.makeTree(menuList,-1L);
    }


}
