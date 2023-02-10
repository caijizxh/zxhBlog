package com.zxh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxh.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-15 23:45:05
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long id);
}

