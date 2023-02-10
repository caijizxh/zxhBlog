package com.zxh.service;

import com.zxh.domain.ResponseResult;
import com.zxh.domain.entity.User;

public interface AdminLoginService{
    ResponseResult login(User user);

    ResponseResult logout();
}
