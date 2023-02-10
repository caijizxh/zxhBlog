package com.zxh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxh.constants.RedisConstants;
import com.zxh.constants.SysyemConstants;
import com.zxh.domain.ResponseResult;
import com.zxh.domain.dto.ArticleAddDto;
import com.zxh.domain.dto.ArticleSingleDto;
import com.zxh.domain.entity.Article;
import com.zxh.domain.entity.ArticleTag;
import com.zxh.domain.entity.Category;
import com.zxh.domain.entity.Tag;
import com.zxh.domain.vo.ArticleListVo;
import com.zxh.domain.vo.DetailArticleVo;
import com.zxh.domain.vo.HotArticleVo;
import com.zxh.domain.vo.PageVo;
import com.zxh.mapper.ArticleMapper;
import com.zxh.mapper.ArticleTagMapper;
import com.zxh.mapper.TagMapper;
import com.zxh.service.ArticleService;
import com.zxh.service.ArticleTagService;
import com.zxh.service.CategoryService;
import com.zxh.utils.BeanCopyUtils;

import com.zxh.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    /*
    * 热门文章推荐列表
    * */
    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章 封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus, SysyemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多只查询10条
        Page page = new Page(1,10);
        Page p = page(page, queryWrapper);
        List<Article> articles = p.getRecords();
        /*List<HotArticleVo> hotArticleVos = new ArrayList<>();
        for (Article article : articles) {
            HotArticleVo hotArticleVo = new HotArticleVo();
            BeanUtils.copyProperties(article,hotArticleVo);
            hotArticleVos.add(hotArticleVo);
        }*/
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
        return ResponseResult.okResult(hotArticleVos);
    }
    /*
    * 博文封页展示
    * */
    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        LambdaQueryWrapper<Article> ArtLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //1、判断博文是已经发出的文章
        ArtLambdaQueryWrapper.eq(Article::getStatus,SysyemConstants.ARTICLE_STATUS_NORMAL);
        //2、判断是否存在categoryId
        ArtLambdaQueryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId,categoryId);
        //3、置顶文章优先排列，通过排序方式进行实现
        ArtLambdaQueryWrapper.orderByDesc(Article::getIsTop);
        //3、分页操作
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page, ArtLambdaQueryWrapper);
        List<Article> result = page.getRecords();
        //查询categoryName
        //普通的循环操作
        /*for (Article article : result) {
            Long categoryId1 = article.getCategoryId();
            Category category = categoryService.getById(categoryId1);
            article.setCategoryName(category.getName());
        }*/
        //使用lambda操作
        /*result.stream()
                .map(new Function<Article, Article>() {

                    @Override
                    public Article apply(Article article) {
                        Long cat = article.getCategoryId();
                        Category byId = categoryService.getById(cat);
                        article.setCategoryName(byId.getName());
                        return article;
                    }
                }).collect(Collectors.toList());*/
        //使用lambda操作,精简版
        result.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName())
                ).collect(Collectors.toList());

        //4、VO转换
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(result, ArticleListVo.class);
        articleListVos.stream().forEach(articleListVo-> {
                Integer viewCount = redisCache.getCacheMapValue(RedisConstants.REDIS_ARTICLE_KEY, articleListVo.getId().toString());
                articleListVo.setViewCount(viewCount.longValue());
        });
        PageVo pageVo = new PageVo(articleListVos,page.getSize());
        return ResponseResult.okResult(pageVo);
    }
    /*
    * 博文详情页
    * */
    @Override
    public ResponseResult detailArticle(Integer id) {
        //通过id查询博文内容
        Article article = getById(id);
        Integer viewCount = redisCache.getCacheMapValue(RedisConstants.REDIS_ARTICLE_KEY, id.toString());
        article.setViewCount(viewCount.longValue());
        //VO操作
        DetailArticleVo detailArticleVo = BeanCopyUtils.copyBean(article, DetailArticleVo.class);
        //根据分类id查询分类名
        Category category = categoryService.getById(article.getCategoryId());
        if (category!=null){
            detailArticleVo.setCategoryName(category.getName());
        }
        return ResponseResult.okResult(detailArticleVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.incrementCacheMapValue(RedisConstants.REDIS_ARTICLE_KEY,id.toString(),1);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult add(ArticleAddDto articleAddDto) {
        //先将数据保存到数据库中
        Article article = BeanCopyUtils.copyBean(articleAddDto, Article.class);
        save(article);
        //解决多个标签对应多篇文章
        List<ArticleTag> collect = articleAddDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(collect);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult articleListAdmin(Integer pageNum, Integer pageSize, String title, String summary) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(title),Article::getTitle,title);
        queryWrapper.like(StringUtils.hasText(summary),Article::getSummary,summary);
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<Article> records = page.getRecords();
        PageVo pageVo = new PageVo(records, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getUserInfoById(Long id) {
        Article article = getById(id);
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> articleTags = articleTagMapper.selectList(queryWrapper);
        List<Long> collect = articleTags.stream()
                .map(articleTag -> articleTag.getTagId())
                .collect(Collectors.toList());
        ArticleSingleDto articleSingleDto = BeanCopyUtils.copyBean(article, ArticleSingleDto.class);
        articleSingleDto.setTags(collect);
        return ResponseResult.okResult(articleSingleDto);
    }

    @Override
    @Transactional
    public ResponseResult updateArticleAdmin(ArticleSingleDto articleSingleDto) {
        Long id = articleSingleDto.getId();
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,id);
        articleTagService.remove(queryWrapper);
        Article article = BeanCopyUtils.copyBean(articleSingleDto, Article.class);
        updateById(article);
        List<Long> tags = articleSingleDto.getTags();
        List<ArticleTag> collect = tags.stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(collect);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteArticleAdmin(Long id) {
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,id).set(Article::getDelFlag,SysyemConstants.DELETE_FLAG);
        update(null,updateWrapper);
        return ResponseResult.okResult();
    }
}
