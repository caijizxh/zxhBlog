package com.zxh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxh.constants.SysyemConstants;
import com.zxh.domain.ResponseResult;
import com.zxh.domain.dto.MenuInfoDto;
import com.zxh.domain.dto.MenuTreeDto;
import com.zxh.domain.dto.RoleMenuDto;
import com.zxh.domain.entity.Article;
import com.zxh.domain.entity.Menu;
import com.zxh.domain.entity.RoleMenu;
import com.zxh.domain.vo.MenuVo;
import com.zxh.mapper.MenuMapper;
import com.zxh.service.MenuService;
import com.zxh.service.RoleMenuService;
import com.zxh.utils.BeanCopyUtils;
import com.zxh.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2022-11-15 23:42:01
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private RoleMenuService roleMenuService;
    @Override
    public List<String> selectPermsByUserId(Long id) {
        //首先判断其id是否为1，若是1则是超级用户
        if (SecurityUtils.isAdmin()){
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType,SysyemConstants.MENU_TYPE_C,SysyemConstants.MENU_TYPE_F);
            queryWrapper.eq(Menu::getStatus, SysyemConstants.STATUS_NORMAL);
            List<Menu> list = list(queryWrapper);
            List<String> collect = list.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return collect;
        }
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> list = null;
        //判断该用户是否为超级用户
        if (SecurityUtils.isAdmin()){
            //如果userId值为1L，则表示其为超级用户
            list = menuMapper.selectAllRouterMenu();
        }else {
            //否则  通过该用户的id值获取当前用户所具有的Menu
            list = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        //获得List类型的变量后，需要将其转化为tree
        //先找出第一层的menu，然后再根据第一层找出他们的子类目录进行设置children
        List<Menu> menus = builderMenuTree(list,SysyemConstants.MENU_ROOT_VALUE);
        return menus;
    }

    @Override
    public ResponseResult menuList(String status, String menuName) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(status),Menu::getStatus,status);
        queryWrapper.like(StringUtils.hasText(menuName),Menu::getMenuName,menuName);
        queryWrapper.orderByAsc(Menu::getParentId).orderByAsc(Menu::getOrderNum);
        List<Menu> list = list(queryWrapper);
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(list, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }

    @Override
    public ResponseResult addMenu(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getMenuInfo(Long id) {
        Menu menu = getById(id);
        MenuInfoDto menuInfoDto = BeanCopyUtils.copyBean(menu, MenuInfoDto.class);
        return ResponseResult.okResult(menuInfoDto);
    }

    @Override
    public ResponseResult updateMenuInfo(Menu menu) {
        if (menu.getParentId().equals(menu.getId())){
            throw new RuntimeException("修改菜单'写博文'失败，上级菜单不能选择自己");
        }
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenuInfo(Long menuId) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,menuId);
        List<Menu> list = list(queryWrapper);
        if (!SysyemConstants.MENU_SIZE_DELETE.equals(list.size())){
            throw new RuntimeException("存在子菜单不允许删除");
        }
        LambdaUpdateWrapper<Menu> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Menu::getId,menuId).set(Menu::getDelFlag,SysyemConstants.DELETE_FLAG);
        update(null,updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult treeselect() {
        List<MenuTreeDto> menus = getMenuTree();
        return ResponseResult.okResult(menus);
    }

    private List<MenuTreeDto> getMenuTree() {
        List<Menu> list = list();
        List<MenuTreeDto> menuTreeDtos = MenucopyBeanList(list, MenuTreeDto.class);
        List<MenuTreeDto> menus = builderMenuTreeDto(menuTreeDtos,SysyemConstants.MENU_ROOT_VALUE);
        return menus;
    }

    @Override
    public ResponseResult roleMenuTreeselect(Long id) {
        List<MenuTreeDto> treeselect = getMenuTree();
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,id);
        List<RoleMenu> list = roleMenuService.list(queryWrapper);
        List<Long> collect = list.stream()
                .map(roleMenu -> roleMenu.getMenuId())
                .collect(Collectors.toList());
        RoleMenuDto roleMenuDto = new RoleMenuDto(treeselect, collect);
        return ResponseResult.okResult(roleMenuDto);
    }

    private List<MenuTreeDto> MenucopyBeanList(List<Menu> list, Class<MenuTreeDto> menuTreeDtoClass) {
        return list.stream()
                .map(o -> copyBean(o, menuTreeDtoClass))
                .collect(Collectors.toList());
    }
    private MenuTreeDto copyBean(Menu o, Class<MenuTreeDto> clazz){
        MenuTreeDto t = null;
        try {
            t = clazz.newInstance();
            t.setLabel(o.getMenuName());
            BeanUtils.copyProperties(o,t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    private List<MenuTreeDto> builderMenuTreeDto(List<MenuTreeDto> menuTreeDtos, Long menuRootValue) {
        //使用流操作
        List<MenuTreeDto> treeMenu = menuTreeDtos.stream()
                //利用过滤器过滤出只属于该用户的信息
                .filter(menu -> menu.getParentId().equals(menuRootValue))
                //利用map操作设置属于上面父类id的子类，setxxx方法没有返回值，但此处需要返回值，故在实现过程中利用lombok中的Access方法设置可以返回的setxxx方法
                .map(menu -> menu.setChildren(getChildrenDto(menu, menuTreeDtos)))
                //最终形成集合的形式
                .collect(Collectors.toList());
        return treeMenu;
    }

    private List<MenuTreeDto> getChildrenDto(MenuTreeDto menu, List<MenuTreeDto> menuTreeDtos) {
        List<MenuTreeDto> childrenList = menuTreeDtos.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> m.setChildren(getChildrenDto(m, menuTreeDtos)))
                .collect(Collectors.toList());
        return childrenList;
    }

    private List<Menu> builderMenuTree(List<Menu> list, Long menuRootValue) {
        //使用流操作
        List<Menu> treeMenu = list.stream()
                //利用过滤器过滤出只属于该用户的信息
                .filter(menu -> menu.getParentId().equals(menuRootValue))
                //利用map操作设置属于上面父类id的子类，setxxx方法没有返回值，但此处需要返回值，故在实现过程中利用lombok中的Access方法设置可以返回的setxxx方法
                .map(menu -> menu.setChildren(getChildren(menu, list)))
                //最终形成集合的形式
                .collect(Collectors.toList());
        return treeMenu;
    }

    /**
     * 获取存入参数的 子Menu集合
     * @param menu
     * @param list
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> list) {
        List<Menu> childrenList = list.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> m.setChildren(getChildren(m, list)))
                .collect(Collectors.toList());
        return childrenList;
    }
}
