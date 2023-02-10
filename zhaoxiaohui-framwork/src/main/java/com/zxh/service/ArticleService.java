package com.zxh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxh.domain.ResponseResult;
import com.zxh.domain.dto.ArticleAddDto;
import com.zxh.domain.dto.ArticleSingleDto;
import com.zxh.domain.entity.Article;

public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult detailArticle(Integer id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(ArticleAddDto articleAddDto);

    ResponseResult articleListAdmin(Integer pageNum, Integer pageSize, String title, String summary);

    ResponseResult getUserInfoById(Long id);

    ResponseResult updateArticleAdmin(ArticleSingleDto articleSingleDto);

    ResponseResult deleteArticleAdmin(Long id);
}
