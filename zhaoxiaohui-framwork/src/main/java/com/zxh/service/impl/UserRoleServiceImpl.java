package com.zxh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxh.domain.entity.UserRole;
import com.zxh.mapper.UserRoleMapper;
import com.zxh.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2022-11-18 02:27:18
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
