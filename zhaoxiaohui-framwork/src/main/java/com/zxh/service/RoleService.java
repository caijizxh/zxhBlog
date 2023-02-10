package com.zxh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxh.domain.ResponseResult;
import com.zxh.domain.dto.RoleAddDto;
import com.zxh.domain.dto.RoleUpdateInfoDto;
import com.zxh.domain.entity.Role;
import com.zxh.domain.vo.ChangeStatusRoleVo;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2022-11-15 23:45:06
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult getRoleList(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult changeStatus(ChangeStatusRoleVo changeStatusRoleVo);

    ResponseResult addRole(RoleAddDto roleAddDto);

    ResponseResult getRoleInfo(Long id);

    ResponseResult updateRoleInfo(RoleUpdateInfoDto roleUpdateInfoDto);

    ResponseResult deleteRoleInfo(Long id);

    ResponseResult listAllRole();
}

