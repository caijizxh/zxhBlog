package com.zxh.service.impl;


import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.zxh.domain.ResponseResult;
import com.zxh.enums.AppHttpCodeEnum;
import com.zxh.exception.SystemException;
import com.zxh.service.UploadService;
import com.zxh.utils.PathUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

@Service
@ConfigurationProperties(prefix = "oss")
@Data
public class UploadServiceImpl implements UploadService {
    @Override
    public ResponseResult upload(MultipartFile img) {
        //判断文件类型
        //获得原始文件的全称
        String filename = img.getOriginalFilename();
        if (!filename.endsWith(".png")){
            throw new SystemException(AppHttpCodeEnum.UPLOAD_FILETYPE_ERROR);
        }
        //如果判断通过将内容上传到oss
        String filePath = PathUtils.generateFilePath(filename);
        String s = imgUpload(img, filePath);
        return ResponseResult.okResult(s);
    }

    private String accessKey;
    private String secretKey;
    private String bucket;

    private String imgUpload(MultipartFile imgFile, String fileName){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());

        UploadManager uploadManager = new UploadManager(cfg);

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = fileName;

        try {
            InputStream inputStream = imgFile.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(inputStream, key, upToken,null,null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return "http://rl52mwd50.hb-bkt.clouddn.com/"+putRet.key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (UnsupportedEncodingException ex) {
            //ignore
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "www";
    }
}
