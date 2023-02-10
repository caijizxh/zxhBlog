package com.zxh.Controller;

import com.zxh.domain.ResponseResult;
import com.zxh.domain.dto.RoleAddDto;
import com.zxh.domain.dto.RoleUpdateInfoDto;
import com.zxh.domain.vo.ChangeStatusRoleVo;
import com.zxh.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @GetMapping("/list")
    public ResponseResult getRoleList(Integer pageNum, Integer pageSize, String roleName, String status){
        return roleService.getRoleList(pageNum,pageSize,roleName,status);
    }
    @PutMapping("/changeStatus")
    public  ResponseResult changeStatus(@RequestBody ChangeStatusRoleVo changeStatusRoleVo){
        return roleService.changeStatus(changeStatusRoleVo);
    }
    @PostMapping
    public ResponseResult addRole(@RequestBody RoleAddDto roleAddDto){
        return roleService.addRole(roleAddDto);
    }
    @GetMapping("/{id}")
    public ResponseResult getRoleInfo(@PathVariable("id")Long id){
        return roleService.getRoleInfo(id);
    }
    @PutMapping
    public ResponseResult updateRoleInfo(@RequestBody RoleUpdateInfoDto roleUpdateInfoDto){
        return roleService.updateRoleInfo(roleUpdateInfoDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteRoleInfo(@PathVariable("id") Long id){
        return roleService.deleteRoleInfo(id);
    }
    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return roleService.listAllRole();
    }
}
