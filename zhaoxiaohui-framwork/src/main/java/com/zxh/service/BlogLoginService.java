package com.zxh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxh.domain.ResponseResult;
import com.zxh.domain.entity.User;


public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
