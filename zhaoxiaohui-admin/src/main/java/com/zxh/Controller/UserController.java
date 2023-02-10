package com.zxh.Controller;

import com.zxh.domain.ResponseResult;
import com.zxh.domain.dto.UserAddInfoDto;
import com.zxh.domain.dto.UserAdminListDto;
import com.zxh.domain.dto.UserUpdateDto;
import com.zxh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/list")
    public ResponseResult getUeserList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status){
        return userService.getUeserList(pageNum,pageSize,userName,phonenumber,status);
    }
    @PostMapping
    public ResponseResult addUserAdmin(@RequestBody UserAddInfoDto userAddInfoDto){
        return userService.addUserAdmin(userAddInfoDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteUserAdmin(@PathVariable("id") Long id){
        return userService.deleteUserAdmin(id);
    }
    @GetMapping("/{id}")
    public ResponseResult getUserInfo(@PathVariable("id") Long id){
        return userService.getUserInfo(id);
    }
    @PutMapping
    public ResponseResult updateUserInfoAdmin(@RequestBody UserUpdateDto userUpdateDto){
        return userService.updateUserInfoAdmin(userUpdateDto);
    }
}
