package com.zxh.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailArticleVo {

    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 是否允许评论 1是，0否
     */
    private String isComment;
    /**
     * 文章内容
     */
    private String content;
    /**
     * 所属分类id
     */
    private Long categoryId;
    /**
     * 所属分类name
     */
    private String categoryName;
    /**
     * 访问量
     */
    private Long viewCount;

    private Date createTime;
}
