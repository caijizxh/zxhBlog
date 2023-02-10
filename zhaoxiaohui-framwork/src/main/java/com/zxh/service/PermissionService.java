package com.zxh.service;


import com.zxh.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionService {

    public Boolean hasPermission(String Permission){
        //如果是超级用户，直接返回true
        if (SecurityUtils.isAdmin()){
            return true;
        }
        //否则 获取当前登录用户所具有的权限列表
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(Permission);
    }
}
