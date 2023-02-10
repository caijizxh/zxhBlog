package com.zxh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxh.domain.entity.User;
import org.springframework.stereotype.Component;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-09 15:34:54
 */
@Component
public interface UserMapper extends BaseMapper<User> {

}

