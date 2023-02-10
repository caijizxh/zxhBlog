package com.zxh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxh.domain.ResponseResult;
import com.zxh.domain.dto.TagDto;
import com.zxh.domain.entity.Tag;
import com.zxh.domain.vo.PageVo;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2022-11-15 17:29:35
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> getTagList(Integer pageNume, Integer pageSize, TagDto tagDto);

    ResponseResult insertTag(TagDto tagDto);

    ResponseResult deleteTag(Long tagId);

    ResponseResult getTag(Long tagId);

    ResponseResult updateTag(Tag tag);

    ResponseResult listAllTag();
}

