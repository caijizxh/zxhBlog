package com.zxh.controller;

import com.zxh.domain.ResponseResult;
import com.zxh.domain.entity.Article;
import com.zxh.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
   /* @RequestMapping("/list")
    public List<Article> testArticle(){
        return articleService.list();
    }*/
    /*
    * 热门博文展示
    * */
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){
        return articleService.hotArticleList();
    }
    /*
    *所有博文封面展示
     */
    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId){
        return articleService.articleList(pageNum, pageSize,categoryId);
    }
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id")Long id){
        return articleService.updateViewCount(id);
    }
    /*
    * 文章详情接口
    * */
    @GetMapping("/{id}")
    public ResponseResult detailArticle(@PathVariable("id") Integer id){
        return articleService.detailArticle(id);
    }
}
