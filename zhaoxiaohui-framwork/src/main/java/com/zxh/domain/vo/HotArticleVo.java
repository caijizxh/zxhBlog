package com.zxh.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotArticleVo {
    private Long id;
    /**
     * 标题
     */
    private String title;

    /**
     * 访问量
     */
    private Long viewCount;
    /**
     * 状态（0已发布，1草稿）
     */
    private String status;
}
