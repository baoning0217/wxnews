package com.rietergroup.wxnews.controller;

import com.rietergroup.wxnews.async.EventModel;
import com.rietergroup.wxnews.async.EventProducer;
import com.rietergroup.wxnews.async.EventType;
import com.rietergroup.wxnews.service.NewsService;
import com.rietergroup.wxnews.service.UserService;
import com.rietergroup.wxnews.util.WxnewsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private NewsService newsService;

    @Autowired
    private UserService userService;

    @Autowired
    EventProducer eventProducer;

    /**
     * 注册用户
     */
    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String reg(Model model,HttpServletResponse response,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember",defaultValue = "0") int rememberme){
        try{
            Map<String,Object> map = userService.register(username, password);
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if(rememberme > 0){
                    cookie.setMaxAge(3600*24*1);
                }
                response.addCookie(cookie);
                return WxnewsUtil.getJSONString(0,"注册成功");
            }else {
                return WxnewsUtil.getJSONString(1,map);
            }
        }catch (Exception e){
            logger.error("注册异常" + e.getMessage());
            return WxnewsUtil.getJSONString(1,"注册异常");
        }
    }


    /**
     * 登录用户
     */
    @RequestMapping(path = {"/login/"}, method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String login(Model model,HttpServletResponse response,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember",defaultValue = "0") int rememberme ){
        try{
            Map<String,Object> map = userService.login(username, password);
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if(rememberme > 0){
                    cookie.setMaxAge(3600*24*1);
                }
                response.addCookie(cookie);
                eventProducer.fireEvent(new EventModel(EventType.LOGIN)
                        .setActorId((int) map.get("userId"))
                        .setExt("username", username)
                        .setExt("email", "1611193688@qq.com" ));
                return WxnewsUtil.getJSONString(0,"登录成功");
            }else {
                return WxnewsUtil.getJSONString(1,map);
            }
        }catch (Exception e){
            logger.error("登录失败:" + e.getMessage());
            return WxnewsUtil.getJSONString(1,"登录失败");
        }
    }


    /**
     * 注销用户
     */
    @RequestMapping(path = {"/logout/"}, method = {RequestMethod.GET,RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }


}
