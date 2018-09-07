package com.rietergroup.wxnews.intercaptor;

import com.rietergroup.wxnews.dao.LoginTicketDao;
import com.rietergroup.wxnews.dao.UserDao;
import com.rietergroup.wxnews.model.HostHolder;
import com.rietergroup.wxnews.model.LoginTicket;
import com.rietergroup.wxnews.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;


    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if(hostHolder.getUser() == null){
            httpServletResponse.sendRedirect("/?pop=1");
            return false;
        }
        return true;
    }


    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }


    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
