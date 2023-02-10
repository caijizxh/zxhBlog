package com.zxh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxh.domain.entity.ArticleTag;
import org.springframework.stereotype.Component;


/**
 * 文章标签关联表(ArticleTag)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-17 11:01:00
 */
@Component
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

}

