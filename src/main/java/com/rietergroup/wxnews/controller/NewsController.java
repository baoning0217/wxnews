package com.rietergroup.wxnews.controller;

import com.rietergroup.wxnews.dao.CommentDao;
import com.rietergroup.wxnews.model.*;
import com.rietergroup.wxnews.service.*;
import com.rietergroup.wxnews.util.WxnewsUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class NewsController {

    @Autowired
    private NewsService newsService;

    @Autowired
    UserService userService;

    @Autowired
    private QiniuService qiniuService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;


    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    /**
     * 图片展示
     * @param imageName
     */
    @RequestMapping(path = {"/image/"}, method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName, HttpServletResponse response){
        response.setContentType("image/jpeg");
        try{
            StreamUtils.copy(new FileInputStream(new File(WxnewsUtil.IMAGE_DIR + imageName)), response.getOutputStream());
        }catch (Exception e){
            logger.error("读取图片错误" + e.getMessage());
        }
    }


    /**
     *上传图片
     */
    @RequestMapping(path = {"/uploadImage/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file){
        try{
            //String fileUrl = newsService.saveImage(file);
            String fileUrl = qiniuService.saveImage(file);
            if(fileUrl == null){
                return WxnewsUtil.getJSONString(1, "上传图片失败");
            }
            return WxnewsUtil.getJSONString(0, fileUrl);
        }catch(Exception e) {
            logger.error("上传图片失败" + e.getMessage());
            return WxnewsUtil.getJSONString(1, "上传失败");
        }
    }

    /**
     *发布新闻
     */
    @RequestMapping(path = {"/user/addNews/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link){
        try{
            News news = new News();
            if(hostHolder.getUser() != null){
                news.setUserId(hostHolder.getUser().getId());
            }else{
                //匿名id
                news.setUserId(3);
            }
            news.setImage(image);
            news.setCreateDate(new Date());
            news.setTitle(title);
            news.setLink(link);
            newsService.addNews(news);
            return WxnewsUtil.getJSONString(0);
        }catch (Exception e){
            logger.error("添加资讯错误" + e.getMessage());
            return WxnewsUtil.getJSONString(1,"发布失败");
        }
    }

    /**
     *资讯详情
     */
    @RequestMapping(path = {"/news/{newsId}"}, method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId, Model model){
        News news = newsService.getById(newsId);

        if(news != null){
            int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
            if(localUserId != 0){
                model.addAttribute("like",likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
            }else{
                model.addAttribute("like", 0);
            }

            //评论
            List<Comment> comments = commentService.getCommentByEntity(news.getId(), EntityType.ENTITY_NEWS);
            List<ViewObject> commentVOs = new ArrayList<ViewObject>();
            for(Comment comment : comments){
                ViewObject vo = new ViewObject();
                vo.set("comment", comment);
                vo.set("user", userService.getUser(comment.getUserId()));
                commentVOs.add(vo);
            }
            model.addAttribute("comments", commentVOs);
        }
        model.addAttribute("news", news);
        model.addAttribute("owner", userService.getUser(news.getUserId()));

        return "detail";
    }


    @RequestMapping(path = {"/addComment}"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId, @RequestParam("content") String content){
        try{
            Comment comment = new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setCreateDate(new Date());
            comment.setStatus(0);

            commentService.addComment(comment);
            //更新news中评论数
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            newsService.updateCommentCount(comment.getEntityId(),count);
        }catch(Exception e){
            logger.error("增加评论失败" + e.getMessage());
        }
        return "redirect:/news/" + String.valueOf(newsId);
    }




}
