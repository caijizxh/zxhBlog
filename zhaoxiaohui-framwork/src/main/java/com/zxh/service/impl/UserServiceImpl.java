package com.zxh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxh.constants.SysyemConstants;
import com.zxh.domain.ResponseResult;
import com.zxh.domain.dto.UserAddInfoDto;
import com.zxh.domain.dto.UserAdminListDto;
import com.zxh.domain.dto.UserUpdateDto;
import com.zxh.domain.entity.Role;
import com.zxh.domain.entity.RoleMenu;
import com.zxh.domain.entity.User;
import com.zxh.domain.entity.UserRole;
import com.zxh.domain.vo.PageVo;
import com.zxh.domain.vo.UserGetByIdVo;
import com.zxh.domain.vo.UserGetInfoVo;
import com.zxh.domain.vo.UserInfoVo;
import com.zxh.enums.AppHttpCodeEnum;
import com.zxh.exception.SystemException;
import com.zxh.mapper.UserMapper;
import com.zxh.service.RoleService;
import com.zxh.service.UserRoleService;
import com.zxh.service.UserService;
import com.zxh.utils.BeanCopyUtils;
import com.zxh.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-11-10 00:45:14
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;
    @Override
    public ResponseResult userInfo() {
        Long userId = SecurityUtils.getUserId();
        User user = getById(userId);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        /*UserMapper baseMapper = getBaseMapper();
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId,user.getId()).set(User::getNickName,user.getNickName())
                .set(User::getSex,user.getSex()).set(User::getAvatar,user.getAvatar());
        baseMapper.update(null,updateWrapper);*/
        return ResponseResult.okResult();
    }
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public ResponseResult register(User user) {
        //判断注册的内容是否为空
        if (!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NULL);
        }
        //查询用户名是否出现重复
        if (userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (EmailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        if (nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        //将密码进行加密，然后进行存储
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUeserList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(phonenumber),User::getPhonenumber,phonenumber);
        queryWrapper.like(StringUtils.hasText(userName),User::getUserName,userName);
        queryWrapper.eq(StringUtils.hasText(status),User::getStatus,status);
        Page<User> page = new Page<>(pageNum, pageSize);
        page(page,queryWrapper);
        List<User> records = page.getRecords();
        List<UserAdminListDto> userAdminListDtos = BeanCopyUtils.copyBeanList(records, UserAdminListDto.class);
        PageVo pageVo = new PageVo(userAdminListDtos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    @Transactional
    public ResponseResult addUserAdmin(UserAddInfoDto userAddInfoDto) {
        if (!StringUtils.hasText(userAddInfoDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NULL);
        }
        if (userNameExist(userAddInfoDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (EmailExist(userAddInfoDto.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        if (PhoneNumeExist(userAddInfoDto.getPhonenumber())){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        //将密码进行加密，然后进行存储
        String encode = passwordEncoder.encode(userAddInfoDto.getPassword());
        userAddInfoDto.setPassword(encode);
        User user = BeanCopyUtils.copyBean(userAddInfoDto, User.class);
        //存入数据库
        UserMapper baseMapper = getBaseMapper();
        baseMapper.insert(user);
        //添加用户与role的关系
        List<Long> roleIds = userAddInfoDto.getRoleIds();
        List<UserRole> collect = roleIds.stream()
                .map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(collect);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteUserAdmin(Long id) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId,id).set(User::getDelFlag, SysyemConstants.DELETE_FLAG);
        update(updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserInfo(Long id) {
        User user = getById(id);
        UserGetByIdVo userGetByIdVo = BeanCopyUtils.copyBean(user, UserGetByIdVo.class);
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,id);
        List<UserRole> list = userRoleService.list(queryWrapper);
        List<Long> roleIds = list.stream()
                .map(userRole -> userRole.getRoleId())
                .collect(Collectors.toList());
        List<Role> roles = roleService.list();
        UserGetInfoVo userGetInfoVo = new UserGetInfoVo(roleIds, roles, userGetByIdVo);
        return ResponseResult.okResult(userGetInfoVo);
    }

    @Override
    @Transactional
    public ResponseResult updateUserInfoAdmin(UserUpdateDto userUpdateDto) {
        Long id = userUpdateDto.getId();
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,id);
        userRoleService.remove(queryWrapper);
        User user = BeanCopyUtils.copyBean(userUpdateDto, User.class);
        updateById(user);
        List<Long> roleIds = userUpdateDto.getRoleIds();
        List<UserRole> collect = roleIds.stream()
                .map(roleId->new UserRole(id,roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(collect);
        return ResponseResult.okResult();
    }

    private boolean PhoneNumeExist(String phonenumber) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhonenumber,phonenumber);
        int count = count(queryWrapper);
        if (count>0){
            return true;
        }
        return false;
    }

    private boolean EmailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);
        int count = count(queryWrapper);
        if (count>0){
            return true;
        }
        return false;
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,nickName);
        int count = count(queryWrapper);
        if (count>0){
            return true;
        }
        return false;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        int count = count(queryWrapper);
        if (count>0){
            return true;
        }
        return false;
    }
}
