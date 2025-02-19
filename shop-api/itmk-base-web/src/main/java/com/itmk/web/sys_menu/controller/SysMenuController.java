package com.itmk.web.sys_menu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.sys_menu.entity.MakeMenuTree;
import com.itmk.web.sys_menu.entity.PermissonVo;
import com.itmk.web.sys_menu.entity.SysMenu;
import com.itmk.web.sys_menu.service.SysMenuService;
import com.itmk.web.sys_user.entity.SysUser;
import com.itmk.web.sys_user.service.SysUserService;
import com.itmk.web.user_menu.entity.AssignParm;
import com.itmk.web.user_menu.service.UserMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/menu")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private UserMenuService userMenuService;

    //新增
    @PostMapping
    public ResultVo add(@RequestBody SysMenu sysMenu) {
        sysMenu.setCreateTime(new Date());
        if(sysMenuService.save(sysMenu)){
            return ResultUtils.success("新增成功！");
        }
        return ResultUtils.error("新增失败！");
    }
    //编辑
    @PutMapping
    public ResultVo edit(@RequestBody SysMenu sysMenu) {
        if(sysMenuService.updateById(sysMenu)){
            return ResultUtils.success("编辑成功！");
        }
        return ResultUtils.error("编辑失败！");
    }
    //删除
    @DeleteMapping("/{menuId}")
    public ResultVo delete(@PathVariable("menuId") Long menuId) {
        if(sysMenuService.removeById(menuId)){
            return ResultUtils.success("删除成功！");
        }
        return ResultUtils.error("删除失败！");
    }
    //列表
    @GetMapping("/list")
    public ResultVo list() {
        QueryWrapper<SysMenu> query= new QueryWrapper<>();
        query.lambda().orderByAsc(SysMenu::getOrderNum);
        List<SysMenu> menuList = sysMenuService.list(query);
        //组装树数据
        List<SysMenu> list= MakeMenuTree.makeTree(menuList,0L);
        return ResultUtils.success("查询成功", list);
    }
    //查询上级菜单
    @GetMapping("/getParent")
    public ResultVo getParent() {
        List<SysMenu> parent = sysMenuService.getParent();
        return ResultUtils.success("查询成功", parent);
    }
    //分配菜单树数据查询与回显
    @GetMapping("/getAssignTree")
    public ResultVo getAssignTree(Long userId,Long assId) {
        //查询当前用户信息
        SysUser user = sysUserService.getById(userId);
        //查询菜单信息
        List<SysMenu> menuList=null;
        //判断用户是否是超级管理员，超级管理员拥有所有权限
        if(StringUtils.isNotEmpty(user.getIsAdmin())&&"1".equals(user.getIsAdmin())){
            menuList=sysMenuService.list();
        }else{
            //根据用户Id查询
            menuList=sysMenuService.getMenuByUserId(userId);
        }
        //组装树数据
        List<SysMenu> menus=MakeMenuTree.makeTree(menuList,0L);
        //
        PermissonVo vo=new PermissonVo();
        vo.setMenuList(menus);
        //查询回显数据
        List<SysMenu> menuByUserId = sysMenuService.getMenuByUserId(assId);
        List<Long> ids=new ArrayList<>();
        Optional.ofNullable(menuByUserId).orElse(new ArrayList<>())
                .stream()
                .filter(item->item!=null)
                .forEach(item->{
                    ids.add(item.getMenuId());
        });
        vo.setCheckList(ids.toArray());
        return ResultUtils.success("查询成功！",vo);
    }
    //分配菜单保存
    @PostMapping("/assignSave")
    public ResultVo assignSave(@RequestBody AssignParm parm) {
//判断是否是超级管理员
        SysUser user = sysUserService.getById(parm.getAssId());
        if (user != null && StringUtils.isNotEmpty(user.getIsAdmin()) &&
                user.getIsAdmin().equals("1")) {
            return ResultUtils.error("当前用户是超级管理员，无需分配菜单!");
        }
        userMenuService.saveMenu(parm);
        return ResultUtils.success("分配菜单成功!");
    }
}
