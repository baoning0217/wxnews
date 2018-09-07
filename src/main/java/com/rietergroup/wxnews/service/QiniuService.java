package com.rietergroup.wxnews.service;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.rietergroup.wxnews.controller.NewsController;
import com.rietergroup.wxnews.util.WxnewsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class QiniuService {

    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    //设置秘钥
    String accessKey = "D0hJOBYmjdRoJEO0GHVRJoHKRxZmU3u5xw7vCYkd";
    String secretKey = "irh79_7jT1FSVWpqWfazv5tlRKlfkVxCJd1hOmNf";

    //设置要上传的空间
    String bucketname = "captainbn";

    //秘钥配置
    Auth auth = Auth.create(accessKey, secretKey);

    //创建上传对象
    UploadManager uploadManager = new UploadManager();

    //简单上传，使用默认策略，
    public String getUpToken(){
        return auth.uploadToken(bucketname);
    }

    public String saveImage(MultipartFile file) throws IOException{
        try{
            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if(dotPos < 0){
                return null;
            }
            String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
            if(!WxnewsUtil.isFileAllowed(fileExt)){
                return null;
            }
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;

            //调用put方法
            Response res = uploadManager.put(file.getBytes(),fileName, getUpToken());

            System.out.println(res.bodyString());
            if(res.isOK() && res.isJson()){
                String key = JSONObject.parseObject(res.bodyString()).get("key").toString();
                return WxnewsUtil.QINIU_DOMAIN_PREFIX + key;
            }else{
                logger.error("七牛异常:" + res.bodyString());
                return null;
            }
        }catch (QiniuException e){
            logger.error("七牛异常:" + e.getMessage());
            return null;
        }
    }


}
