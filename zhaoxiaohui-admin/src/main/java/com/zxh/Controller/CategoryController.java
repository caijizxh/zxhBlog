package com.zxh.Controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.zxh.domain.ResponseResult;
import com.zxh.domain.dto.CategoryAddAdminDto;
import com.zxh.domain.dto.CategoryGetListDto;
import com.zxh.domain.entity.Category;
import com.zxh.domain.vo.ExcelCategoryVo;
import com.zxh.enums.AppHttpCodeEnum;
import com.zxh.service.CategoryService;
import com.zxh.utils.BeanCopyUtils;
import com.zxh.utils.WebUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        return categoryService.listAllCategory();
    }
    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        //首先先设置response头
        try {
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //查询所要写入excel中的内容
            List<Category> list = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(list, ExcelCategoryVo.class);
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
            /*e.printStackTrace();*/
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
    @GetMapping("/list")
    public ResponseResult getAllCategoryList(Integer pageNum, Integer pageSize, String name,String status){
        return categoryService.getAllCategoryList(pageNum,pageSize,name,status);
    }
    @PostMapping
    public ResponseResult addCategory(@RequestBody CategoryAddAdminDto categoryAddAdminDto){
        return categoryService.addCategor(categoryAddAdminDto);
    }
    @GetMapping("/{id}")
    public ResponseResult getCategoryInfo(@PathVariable("id") Long id){
        return categoryService.getCategoryInfo(id);
    }
    @PutMapping
    public ResponseResult updateCategoryInfo(@RequestBody CategoryGetListDto categoryGetListDto){
        return categoryService.updateCategoryInfo(categoryGetListDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable("id")Long id){
        return categoryService.deleteCategory(id);
    }
}
