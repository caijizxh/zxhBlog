package com.zxh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxh.domain.entity.Menu;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-15 23:42:00
 */
@Component
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectAllRouterMenu();

    List<Menu> selectRouterMenuTreeByUserId(Long userId);
}

