package com.zxh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxh.domain.entity.ArticleTag;
import com.zxh.mapper.ArticleTagMapper;
import com.zxh.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2022-11-17 10:57:43
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}

