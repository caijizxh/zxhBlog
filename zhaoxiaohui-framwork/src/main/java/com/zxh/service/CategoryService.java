package com.zxh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxh.domain.ResponseResult;
import com.zxh.domain.dto.CategoryAddAdminDto;
import com.zxh.domain.dto.CategoryGetListDto;
import com.zxh.domain.entity.Category;
import org.springframework.stereotype.Service;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2022-11-08 11:05:30
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult listAllCategory();

    ResponseResult getAllCategoryList(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addCategor(CategoryAddAdminDto categoryAddAdminDto);

    ResponseResult getCategoryInfo(Long id);

    ResponseResult updateCategoryInfo(CategoryGetListDto categoryGetListDto);

    ResponseResult deleteCategory(Long id);
}

