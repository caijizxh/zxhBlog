package com.zxh.Controller;

import com.zxh.domain.ResponseResult;
import com.zxh.domain.dto.ArticleAddDto;
import com.zxh.domain.dto.ArticleSingleDto;
import com.zxh.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @PostMapping
    public ResponseResult add(@RequestBody ArticleAddDto articleAddDto){
        return articleService.add(articleAddDto);
    }
    @GetMapping("/list")
    public ResponseResult articleListAdmin(Integer pageNum, Integer pageSize, String title, String summary){
        return articleService.articleListAdmin(pageNum,pageSize,title,summary);
    }
    @GetMapping("/{id}")
    public ResponseResult getUserInfoById(@PathVariable("id") Long id){
        return articleService.getUserInfoById(id);
    }
    @PutMapping
    public ResponseResult updateArticleAdmin(@RequestBody ArticleSingleDto articleSingleDto){
        return articleService.updateArticleAdmin(articleSingleDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticleAdmin(@PathVariable("id") Long id){
        return articleService.deleteArticleAdmin(id);
    }
}
