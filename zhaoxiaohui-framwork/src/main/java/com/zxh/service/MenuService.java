package com.zxh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxh.domain.ResponseResult;
import com.zxh.domain.dto.MenuTreeDto;
import com.zxh.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2022-11-15 23:42:01
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    ResponseResult menuList(String status, String menuName);

    ResponseResult addMenu(Menu menu);

    ResponseResult getMenuInfo(Long id);

    ResponseResult updateMenuInfo(Menu menu);

    ResponseResult deleteMenuInfo(Long menuId);

    ResponseResult treeselect();

    ResponseResult roleMenuTreeselect(Long id);
}

