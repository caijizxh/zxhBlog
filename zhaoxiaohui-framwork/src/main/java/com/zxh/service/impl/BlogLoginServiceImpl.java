package com.zxh.service.impl;

import com.zxh.constants.RedisConstants;
import com.zxh.domain.ResponseResult;
import com.zxh.domain.entity.LoginUser;
import com.zxh.domain.entity.User;
import com.zxh.domain.vo.BlogUserLoginVo;
import com.zxh.domain.vo.UserInfoVo;
import com.zxh.service.BlogLoginService;
import com.zxh.utils.BeanCopyUtils;
import com.zxh.utils.JwtUtil;
import com.zxh.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {
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
        redisCache.setCacheObject(RedisConstants.REDIS_FLAGNAME+id,loginUser);
        //把user转换为userinfVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        //把token和userinfo封装起来 返回
        BlogUserLoginVo vo = new BlogUserLoginVo(jwt, userInfoVo);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult logout() {
        //获取token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //通过token获取对应的userid
        Long id = loginUser.getUser().getId();
        //根据userid删除redis当中的对应的user
        redisCache.deleteObject(RedisConstants.REDIS_FLAGNAME+id);
        return ResponseResult.okResult();
    }
}
