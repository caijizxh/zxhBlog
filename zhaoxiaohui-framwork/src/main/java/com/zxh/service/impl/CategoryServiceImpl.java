package com.zxh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxh.constants.SysyemConstants;
import com.zxh.domain.ResponseResult;
import com.zxh.domain.dto.CategoryAddAdminDto;
import com.zxh.domain.dto.CategoryDto;
import com.zxh.domain.dto.CategoryGetListDto;
import com.zxh.domain.entity.Article;
import com.zxh.domain.entity.Category;
import com.zxh.domain.entity.Tag;
import com.zxh.domain.vo.CategoryVo;
import com.zxh.domain.vo.PageVo;
import com.zxh.mapper.CategoryMapper;
import com.zxh.service.ArticleService;
import com.zxh.service.CategoryService;
import com.zxh.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2022-11-08 11:06:03
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private ArticleService articleService;
    @Override
    public ResponseResult getCategoryList() {
        LambdaQueryWrapper<Article> ArticleQueryWrapper = new LambdaQueryWrapper<>();
        ArticleQueryWrapper.eq(Article::getStatus, SysyemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> Articlelist = articleService.list(ArticleQueryWrapper);
        Set<Long> collect = Articlelist.stream().map(article -> article.getCategoryId()).collect(Collectors.toSet());

        List<Category> categories = listByIds(collect);

        List<Category> list = categories.stream()
                .filter(category -> category.getStatus().equals(SysyemConstants.STATUS_NORMAL))
                .collect(Collectors.toList());
        //VO封装
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);


        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus,SysyemConstants.CATEGORY_NORMAL);
        List<Category> list = list(queryWrapper);
        List<CategoryDto> categoryDtos = BeanCopyUtils.copyBeanList(list, CategoryDto.class);
        return ResponseResult.okResult(categoryDtos);
    }

    @Override
    public ResponseResult getAllCategoryList(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(status),Category::getStatus,status);
        queryWrapper.like(StringUtils.hasText(name),Category::getName,name);
        Page page = new Page(pageNum, pageSize);
        page(page,queryWrapper);
        List<Category> records = page.getRecords();
        List<CategoryGetListDto> categoryGetListDtos = BeanCopyUtils.copyBeanList(records, CategoryGetListDto.class);
        PageVo pageVo = new PageVo(categoryGetListDtos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addCategor(CategoryAddAdminDto categoryAddAdminDto) {
        Category category = BeanCopyUtils.copyBean(categoryAddAdminDto, Category.class);
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getCategoryInfo(Long id) {
        Category category = getById(id);
        CategoryGetListDto categoryGetListDto = BeanCopyUtils.copyBean(category, CategoryGetListDto.class);
        return ResponseResult.okResult(categoryGetListDto);
    }

    @Override
    public ResponseResult updateCategoryInfo(CategoryGetListDto categoryGetListDto) {
        LambdaUpdateWrapper<Category> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Category::getId,categoryGetListDto.getId()).set(Category::getName,categoryGetListDto.getName()).set(Category::getDescription,categoryGetListDto.getDescription()).set(Category::getStatus,categoryGetListDto.getStatus());
        update(updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteCategory(Long id) {
        LambdaUpdateWrapper<Category> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Category::getId,id).set(Category::getDelFlag,SysyemConstants.DELETE_FLAG);
        update(updateWrapper);
        return ResponseResult.okResult();
    }
}
