package com.zxh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxh.domain.entity.RoleMenu;
import com.zxh.mapper.RoleMenuMapper;
import com.zxh.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2022-11-18 02:19:29
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}
