package com.zxh.job;

import com.zxh.constants.RedisConstants;
import com.zxh.domain.entity.Article;
import com.zxh.service.ArticleService;
import com.zxh.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ArticleViewCountJob {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleService articleService;
    @Scheduled(cron = "0/5 * * * * ?")
    public void articleViewCount(){
        Map<String, Integer> cacheMap = redisCache.getCacheMap(RedisConstants.REDIS_ARTICLE_KEY);
        List<Article> articles = cacheMap.entrySet().stream().map(stringIntegerEntry -> {
            return new Article(Long.valueOf(stringIntegerEntry.getKey()), stringIntegerEntry.getValue().longValue());
        }).collect(Collectors.toList());
        articleService.updateBatchById(articles);
    }
}
