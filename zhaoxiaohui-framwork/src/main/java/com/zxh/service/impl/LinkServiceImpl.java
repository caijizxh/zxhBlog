package com.zxh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxh.constants.SysyemConstants;
import com.zxh.domain.ResponseResult;
import com.zxh.domain.entity.Category;
import com.zxh.domain.entity.Link;
import com.zxh.domain.vo.*;
import com.zxh.mapper.LinkMapper;
import com.zxh.service.LinkService;
import com.zxh.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2022-11-09 10:57:19
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {
    /*
    * 显示友链
    * */
    @Override
    public ResponseResult getAllLink() {
        //根据数据库中的statue进行判断友链是否审核
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SysyemConstants.LINK_STATUS_NORMAL);
        List<Link> list = list(queryWrapper);
        //转换VO
        List<getAllLinkVo> re = BeanCopyUtils.copyBeanList(list, getAllLinkVo.class);
        return new ResponseResult().okResult(re);
    }

    @Override
    public ResponseResult getLinkListAdmin(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name),Link::getName,name);
        queryWrapper.eq(StringUtils.hasText(status),Link::getStatus,status);
        Page<Link> page = new Page<>(pageNum, pageSize);
        page(page,queryWrapper);
        List<Link> records = page.getRecords();
        List<LinkGetListVo> linkGetListVos = BeanCopyUtils.copyBeanList(records, LinkGetListVo.class);
        PageVo pageVo = new PageVo(linkGetListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addLinkAdmin(LinkAddVo linkAddVo) {
        Link link = BeanCopyUtils.copyBean(linkAddVo, Link.class);
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getLinkInfoAdmin(Long id) {
        Link link = getById(id);
        LinkGetListVo linkGetListVo = BeanCopyUtils.copyBean(link, LinkGetListVo.class);
        return ResponseResult.okResult(linkGetListVo);
    }

    @Override
    public ResponseResult updateLinkInfoAdmin(LinkGetListVo linkGetListVo) {
        LambdaUpdateWrapper<Link> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Link::getId,linkGetListVo.getId())
                .set(Link::getStatus,linkGetListVo.getStatus())
                .set(Link::getDescription,linkGetListVo.getDescription())
                .set(Link::getStatus,linkGetListVo.getStatus())
                .set(Link::getName,linkGetListVo.getName())
                .set(Link::getLogo,linkGetListVo.getLogo());
        update(updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteLinkInfoAdmin(Long id) {
        LambdaUpdateWrapper<Link> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Link::getId,id).set(Link::getDelFlag,SysyemConstants.DELETE_FLAG);
        update(updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeLinkStatus(LinkIdStatusVo linkIdStatusVo) {
        LambdaUpdateWrapper<Link> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Link::getId,linkIdStatusVo.getId()).set(Link::getStatus,linkIdStatusVo.getStatus());
        update(updateWrapper);
        return ResponseResult.okResult();
    }
}
