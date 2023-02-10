package com.zxh.Controller;

import com.zxh.domain.ResponseResult;
import com.zxh.domain.entity.Menu;
import com.zxh.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;
    @GetMapping("/list")
    public ResponseResult menuList(String status, String menuName){
        return menuService.menuList(status,menuName);
    }
    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu){
        return menuService.addMenu(menu);
    }
    @GetMapping("/{id}")
    public ResponseResult getMenuInfo(@PathVariable("id") Long id){
        return menuService.getMenuInfo(id);
    }
    @PutMapping
    public ResponseResult updateMenuInfo(@RequestBody Menu menu){
        return menuService.updateMenuInfo(menu);
    }
    @DeleteMapping("/{menuId}")
    public ResponseResult deleteMenuInfo(@PathVariable("menuId") Long menuId){
        return menuService.deleteMenuInfo(menuId);
    }
    @GetMapping("/treeselect")
    public ResponseResult treeselect(){
        return menuService.treeselect();
    }
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeselect(@PathVariable("id")Long id){
        return menuService.roleMenuTreeselect(id);
    }
}
