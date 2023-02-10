package com.zxh.Controller;

import com.zxh.annotation.SystemLog;
import com.zxh.domain.ResponseResult;
import com.zxh.domain.entity.LoginUser;
import com.zxh.domain.entity.Menu;
import com.zxh.domain.entity.User;
import com.zxh.domain.vo.AdminUserInfoVo;
import com.zxh.domain.vo.RoutersVo;
import com.zxh.domain.vo.UserInfoVo;
import com.zxh.enums.AppHttpCodeEnum;
import com.zxh.exception.SystemException;
import com.zxh.service.AdminLoginService;
import com.zxh.service.MenuService;
import com.zxh.service.RoleService;
import com.zxh.utils.BeanCopyUtils;
import com.zxh.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminLoginController {
    @Autowired
    private AdminLoginService adminLoginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    @PostMapping("/user/login")
    @SystemLog(businessName = "博客后台登录方法")
    public ResponseResult login(@RequestBody User user){
        if (!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.COMMENT_CONTENT_NOT_NULL);
        }
        return adminLoginService.login(user);
    }
    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        //获取用户
        User user = SecurityUtils.getLoginUser().getUser();
        Long id = user.getId();
        //通过id获取对应的权限列表
        List<String> perms = menuService.selectPermsByUserId(id);
        //获取权限信息
        List<String> roles = roleService.selectRoleKeyByUserId(id);
        //进行整合
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roles, userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }
    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        //获取对应的用户id
        Long userId = SecurityUtils.getUserId();
        //查询menu 结果是tree的形式
        List<Menu> list = menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据返回
        return ResponseResult.okResult(new RoutersVo(list));
    }
    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return adminLoginService.logout();
    }
}
