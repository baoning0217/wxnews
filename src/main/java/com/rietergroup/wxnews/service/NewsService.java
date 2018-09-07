package com.rietergroup.wxnews.service;

import com.rietergroup.wxnews.dao.NewsDao;
import com.rietergroup.wxnews.model.News;
import com.rietergroup.wxnews.util.WxnewsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {

    @Autowired
    private NewsDao newsDao;

    public List<News> getLatestNews(int userId, int offset, int limit){
        return newsDao.selectByUserIdAndOffset(userId,offset,limit);
    }

    /**
     *上传图片
     */
    public String saveImage(MultipartFile file) throws Exception{
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if(dotPos < 0){
            return null;
        }
        String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
        if(!WxnewsUtil.isFileAllowed(fileExt)){
            return null;
        }
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
        Files.copy(file.getInputStream(), new File(WxnewsUtil.IMAGE_DIR + fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
        return WxnewsUtil.NEWS_DOMAIN + "image?name=" + fileName;
    }

    /**
     *发布资讯
     */
    public int addNews(News news) {
        newsDao.addNews(news);
        return news.getId();
    }

    /**
     * 根据id查询
     */
    public News getById(int newsId){
        return newsDao.getById(newsId);
    }

    /**
     *更新评论数
     */
    public int updateCommentCount(int id, int count) {
        return newsDao.updateCommentCount(id, count);
    }

    public int updateLikeCount(int id, int count){
        return newsDao.updateLikeCount(id, count);
    }


}
