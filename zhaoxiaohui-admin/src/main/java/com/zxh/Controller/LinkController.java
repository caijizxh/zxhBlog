package com.zxh.Controller;

import com.zxh.domain.ResponseResult;
import com.zxh.domain.vo.LinkAddVo;
import com.zxh.domain.vo.LinkGetListVo;
import com.zxh.domain.vo.LinkIdStatusVo;
import com.zxh.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;
    @GetMapping("/list")
    public ResponseResult getLinkListAdmin(Integer pageNum, Integer pageSize, String name, String status){
        return linkService.getLinkListAdmin(pageNum,pageSize,name,status);
    }
    @PostMapping
    public ResponseResult addLinkAdmin(@RequestBody LinkAddVo linkAddVo){
        return linkService.addLinkAdmin(linkAddVo);
    }
    @GetMapping("/{id}")
    public ResponseResult getLinkInfoAdmin(@PathVariable("id")Long id){
        return linkService.getLinkInfoAdmin(id);
    }
    @PutMapping
    public ResponseResult updateLinkInfoAdmin(@RequestBody LinkGetListVo linkGetListVo){
        return linkService.updateLinkInfoAdmin(linkGetListVo);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteLinkInfoAdmin(@PathVariable("id")Long id){
        return linkService.deleteLinkInfoAdmin(id);
    }
    @PutMapping("/changeLinkStatus")
    public ResponseResult changeLinkStatus(@RequestBody LinkIdStatusVo linkIdStatusVo){
        return linkService.changeLinkStatus(linkIdStatusVo);
    }
}
