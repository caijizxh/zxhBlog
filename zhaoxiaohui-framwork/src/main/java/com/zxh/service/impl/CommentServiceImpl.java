package com.zxh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxh.constants.SysyemConstants;
import com.zxh.domain.ResponseResult;
import com.zxh.domain.entity.Comment;
import com.zxh.domain.entity.User;
import com.zxh.domain.vo.CommentVo;
import com.zxh.domain.vo.PageVo;
import com.zxh.enums.AppHttpCodeEnum;
import com.zxh.exception.SystemException;
import com.zxh.mapper.CommentMapper;
import com.zxh.service.CommentService;
import com.zxh.service.UserService;
import com.zxh.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2022-11-09 23:32:51
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private UserService userService;
    @Override
    public ResponseResult commentList(String commentFlag, Long articleId, Integer pageNum, Integer pageSize) {
        //首先通过文章id查出对应的信息
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId, SysyemConstants.COMMENT_ROOT);
        queryWrapper.eq(SysyemConstants.ARTICLE_COMMENT.equals(commentFlag),Comment::getArticleId,articleId);
        queryWrapper.eq(Comment::getType,commentFlag);
        Page<Comment> page = new Page(pageNum,pageSize);
        page(page, queryWrapper);
        List<CommentVo> commentVos = CommentToChange(page.getRecords());
        /*
        * 添加子评论
        * */
        for (CommentVo commentVo : commentVos) {
            List<CommentVo> children = getChildren(commentVo.getId());
            commentVo.setChildren(children);
        }
        return ResponseResult.okResult(new PageVo(commentVos,page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        //判断评论是否存在空值
        if (!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.COMMENT_CONTENT_NOT_NULL);
        }
        //为了提高效率，降低冗余，此处还需要创建一个获取token的一些类
        //在存储之前需要进行缺失值的添加，例如：创建人和创建时间等字段的填充，此处使用mybatis-plus提供的自动填充方式
        //com.zxh.handler.mybatisplus.MyMetaObjectHandler
        save(comment);
        return ResponseResult.okResult();
    }

    private List<CommentVo> getChildren(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId, id);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> children = list(queryWrapper);
        return CommentToChange(children);
    }

    private List<CommentVo> CommentToChange(List<Comment> list){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        for (CommentVo commentVo : commentVos) {
            User userby = userService.getById(commentVo.getCreateBy());
            commentVo.setUsername(userby.getNickName());
            if (commentVo.getToCommentUserId()!=-1){
                String nickName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(nickName);
            }
        }
        return commentVos;
    }
}
