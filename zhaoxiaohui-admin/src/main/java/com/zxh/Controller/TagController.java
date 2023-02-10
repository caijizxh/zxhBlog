package com.zxh.Controller;


import com.zxh.domain.ResponseResult;
import com.zxh.domain.dto.TagDto;
import com.zxh.domain.entity.Tag;
import com.zxh.domain.vo.PageVo;
import com.zxh.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;
    @GetMapping("/list")
    public ResponseResult<PageVo> getTagList(@RequestParam("pageNum") Integer pageNume, Integer pageSize, TagDto tagDto){
        return tagService.getTagList(pageNume,pageSize,tagDto);
    }
    @PostMapping
    public ResponseResult insertTag(@RequestBody TagDto tagDto){
        return tagService.insertTag(tagDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable("id") Long tagId){
        return tagService.deleteTag(tagId);
    }
    @GetMapping("/{id}")
    public ResponseResult getTag(@PathVariable("id")Long tagId){
        return tagService.getTag(tagId);
    }
    @PutMapping
    public ResponseResult updateTag(@RequestBody Tag tag){
        return tagService.updateTag(tag);
    }
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        return tagService.listAllTag();
    }
}
