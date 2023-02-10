package com.zxh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxh.domain.ResponseResult;
import com.zxh.domain.dto.UserAddInfoDto;
import com.zxh.domain.dto.UserAdminListDto;
import com.zxh.domain.dto.UserUpdateDto;
import com.zxh.domain.entity.User;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2022-11-10 00:45:14
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult getUeserList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    ResponseResult addUserAdmin(UserAddInfoDto userAddInfoDto);

    ResponseResult deleteUserAdmin(Long id);

    ResponseResult getUserInfo(Long id);

    ResponseResult updateUserInfoAdmin(UserUpdateDto userUpdateDto);
}

