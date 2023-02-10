package com.zxh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxh.domain.ResponseResult;
import com.zxh.domain.entity.Link;
import com.zxh.domain.vo.LinkAddVo;
import com.zxh.domain.vo.LinkGetListVo;
import com.zxh.domain.vo.LinkIdStatusVo;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2022-11-09 10:57:18
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult getLinkListAdmin(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addLinkAdmin(LinkAddVo linkAddVo);

    ResponseResult getLinkInfoAdmin(Long id);

    ResponseResult updateLinkInfoAdmin(LinkGetListVo linkGetListVo);

    ResponseResult deleteLinkInfoAdmin(Long id);

    ResponseResult changeLinkStatus(LinkIdStatusVo linkIdStatusVo);
}

