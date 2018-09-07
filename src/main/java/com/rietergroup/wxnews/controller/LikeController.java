package com.rietergroup.wxnews.controller;

import com.rietergroup.wxnews.async.EventModel;
import com.rietergroup.wxnews.async.EventProducer;
import com.rietergroup.wxnews.async.EventType;
import com.rietergroup.wxnews.model.EntityType;
import com.rietergroup.wxnews.model.HostHolder;
import com.rietergroup.wxnews.model.News;
import com.rietergroup.wxnews.service.LikeService;
import com.rietergroup.wxnews.service.NewsService;
import com.rietergroup.wxnews.util.WxnewsUtil;
import org.apache.catalina.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * created by baoning on 2018/9/5
 */
@Controller
public class LikeController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    NewsService newsService;

    @Autowired
    EventProducer eventProducer;


    //喜欢
    @RequestMapping(path = {"/like"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("newsId") int newsId) {
        int userId = hostHolder.getUser().getId();
        long likeCount = likeService.like(userId, EntityType.ENTITY_NEWS, newsId);
        News news = newsService.getById(newsId);
        newsService.updateLikeCount(newsId, (int)likeCount);

        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(newsId)
                .setEntityType(EntityType.ENTITY_NEWS)
                .setEntityOwnerId(news.getUserId()));

        return WxnewsUtil.getJSONString(0, String.valueOf(likeCount));
    }

    //不喜欢
    @RequestMapping(path = {"/disLike"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String disLike(@RequestParam("newsId") int newsId) {
        int userId = hostHolder.getUser().getId();
        long likeCount = likeService.disLike(userId, EntityType.ENTITY_NEWS, newsId);
        newsService.updateLikeCount(newsId, (int)likeCount);
        return WxnewsUtil.getJSONString(0, String.valueOf(likeCount));
    }





}