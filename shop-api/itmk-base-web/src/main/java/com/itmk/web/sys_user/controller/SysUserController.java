package com.itmk.web.sys_user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import com.itmk.web.sys_user.entity.PageParm;
import com.itmk.web.sys_user.entity.SysUser;
import com.itmk.web.sys_user.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sysUser")//后面改跨域配置要和这里一致，捕获所有以该链接发送的请求给SysUserController处理
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;
    //新增
    @PostMapping
    public ResultVo add(@RequestBody SysUser sysUser) {
        if(sysUserService.save(sysUser)) {
            return ResultUtils.success("新增成功");
        }
        return ResultUtils.error("新增失败！");
    }
    //编辑
    @PutMapping
    public ResultVo edit(@RequestBody SysUser sysUser) {
        if(sysUserService.updateById(sysUser)) {
            return ResultUtils.success("编辑成功");
        }
        return ResultUtils.error("编辑失败！");
    }
    //删除
    @DeleteMapping("/{userId}")
    public ResultVo delete(@PathVariable("userId") Long userId) {
        if(sysUserService.removeById(userId)) {
            return ResultUtils.success("删除成功");
        }
        return ResultUtils.error("删除失败！");
    }
    //列表
    @GetMapping("/getList")
    public ResultVo getList(PageParm parm){
        //构造查询条件
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        //传入nickname非空才做like判断
        query.lambda().like(StringUtils.isNotEmpty(parm.getNickName()),SysUser::getNickName,parm.getNickName());
        //构造分页对象
        IPage<SysUser> page=new Page<>(parm.getCurrentPage(), parm.getPageSize());
        //查询
        IPage<SysUser> list = sysUserService.page(page, query);
        return ResultUtils.success("查询成功",list);
    }
}
