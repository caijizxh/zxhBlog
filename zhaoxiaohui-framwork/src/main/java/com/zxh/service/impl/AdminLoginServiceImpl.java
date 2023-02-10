package com.zxh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxh.constants.RedisConstants;
import com.zxh.domain.ResponseResult;
import com.zxh.domain.entity.LoginUser;
import com.zxh.domain.entity.User;
import com.zxh.mapper.UserMapper;
import com.zxh.service.AdminLoginService;
import com.zxh.utils.JwtUtil;
import com.zxh.utils.RedisCache;
import com.zxh.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

@Service
public class AdminLoginServiceImpl extends ServiceImpl<UserMapper, User> implements AdminLoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        //是否认证通过
        if (Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误！");
        }
        //获取userid生成token
        LoginUser loginUser= (LoginUser) authenticate.getPrincipal();
        String id = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(id);
        //把用户信息存入到redis
        redisCache.setCacheObject(RedisConstants.ADMIN_REDIS_FLAGNAME+id,loginUser);
        //把token和userinfo封装起来 返回
        HashMap<String, String> map = new HashMap<>();
        map.put("token",jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject(RedisConstants.ADMIN_REDIS_FLAGNAME+userId);
        return ResponseResult.okResult();
    }
}
