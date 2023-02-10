package com.zxh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxh.constants.SysyemConstants;
import com.zxh.domain.ResponseResult;
import com.zxh.domain.dto.*;
import com.zxh.domain.entity.Article;
import com.zxh.domain.entity.ArticleTag;
import com.zxh.domain.entity.Role;
import com.zxh.domain.entity.RoleMenu;
import com.zxh.domain.vo.ChangeStatusRoleVo;
import com.zxh.domain.vo.PageVo;
import com.zxh.domain.vo.RoleVo;
import com.zxh.mapper.RoleMapper;
import com.zxh.service.MenuService;
import com.zxh.service.RoleMenuService;
import com.zxh.service.RoleService;
import com.zxh.utils.BeanCopyUtils;
import com.zxh.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2022-11-15 23:45:06
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private MenuService menuService;
    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        if (SecurityUtils.isAdmin()){
            List<String> list = new ArrayList<>();
            list.add("admin");
            return list;
        }
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult getRoleList(Integer pageNum, Integer pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(status),Role::getStatus,status);
        queryWrapper.like(StringUtils.hasText(roleName),Role::getRoleName,roleName);
        queryWrapper.orderByAsc(Role::getRoleSort);
        queryWrapper.eq(!SysyemConstants.ROLE_PROHIBIT.equals(status),Role::getStatus,SysyemConstants.ROLE_PERSSION);
        Page<Role> page = new Page<>(pageNum, pageSize);
        page(page,queryWrapper);
        List<Role> records = page.getRecords();
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(records, RoleVo.class);
        PageVo pageVo = new PageVo(roleVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult changeStatus(ChangeStatusRoleVo changeStatusRoleVo) {
        LambdaUpdateWrapper<Role> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Role::getId,changeStatusRoleVo.getRoleId()).set(Role::getStatus,changeStatusRoleVo.getStatus());
        update(updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult addRole(RoleAddDto roleAddDto) {
        Role role = BeanCopyUtils.copyBean(roleAddDto, Role.class);
        RoleMapper baseMapper = getBaseMapper();
        baseMapper.insert(role);
        List<String> menuIds = roleAddDto.getMenuIds();
        List<RoleMenu> list = new ArrayList<>();
        for (String menuId : menuIds) {
            list.add(new RoleMenu(role.getId(),Long.valueOf(menuId)));
        }
        roleMenuService.saveBatch(list);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRoleInfo(Long id) {
        Role role = getById(id);
        RoleInfoDto roleInfoDto = BeanCopyUtils.copyBean(role, RoleInfoDto.class);
        return ResponseResult.okResult(roleInfoDto);
    }

    @Override
    @Transactional
    public ResponseResult updateRoleInfo(RoleUpdateInfoDto roleUpdateInfoDto) {
        Long id = roleUpdateInfoDto.getId();
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,id);
        roleMenuService.remove(queryWrapper);
        Role role = BeanCopyUtils.copyBean(roleUpdateInfoDto, Role.class);
        updateById(role);
        List<Long> menuIds = roleUpdateInfoDto.getMenuIds();
        List<RoleMenu> collect = menuIds.stream()
                .map(menuid -> new RoleMenu(role.getId(), menuid))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(collect);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteRoleInfo(Long id) {
        LambdaUpdateWrapper<Role> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Role::getId,id).set(Role::getDelFlag,SysyemConstants.DELETE_FLAG);
        update(updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllRole() {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getStatus,SysyemConstants.STATUS_NORMAL);
        List<Role> list = list(queryWrapper);
        return ResponseResult.okResult(list);
    }
}
