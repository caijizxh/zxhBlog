package com.zxh.runner;

import com.zxh.constants.RedisConstants;
import com.zxh.domain.entity.Article;
import com.zxh.mapper.ArticleMapper;
import com.zxh.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ArticleViewCountRunner implements CommandLineRunner{
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private RedisCache redisCache;
    @Override
    public void run(String... args) throws Exception {
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Integer> collect = articles.stream().collect(Collectors.toMap(article-> {return
                article.getId().toString(); }, article->{
                return article.getViewCount().intValue();
        }));
        redisCache.setCacheMap(RedisConstants.REDIS_ARTICLE_KEY,collect);
    }
}
