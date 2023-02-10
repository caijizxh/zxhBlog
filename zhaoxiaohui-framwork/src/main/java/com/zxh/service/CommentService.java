package com.zxh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxh.domain.ResponseResult;
import com.zxh.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2022-11-09 23:32:51
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentFlag, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}

