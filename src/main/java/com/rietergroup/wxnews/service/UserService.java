package com.rietergroup.wxnews.service;

import com.rietergroup.wxnews.dao.LoginTicketDao;
import com.rietergroup.wxnews.dao.UserDao;
import com.rietergroup.wxnews.model.LoginTicket;
import com.rietergroup.wxnews.model.User;
import com.rietergroup.wxnews.util.WxnewsUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginTicketDao loginTicketDao;

    /**
     * 根据id获取用户
     * @param id
     * @return
     */
    public User getUser(int id){
        return userDao.selectById(id);
    }

    /**
     * 注册用户
     */
    public Map<String,Object> register(String username, String password){
        Map<String,Object> map = new HashMap<String,Object>();
        if(StringUtils.isBlank(username)){
            map.put("msgname","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msgpwd","密码不能为空");
            return map;
        }
        User user = userDao.selectByName(username);
        if(user != null){
            map.put("msgname","用户名已经被注册");
            return map;
        }
        user = new User();
        user.setName(username);
        //加盐加密
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://114.255.95.20/oa/images/test.jpg"));
        user.setPassword(WxnewsUtil.MD5(password + user.getSalt()));
        userDao.addUSer(user);

        //登录
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);

        return map;
    }


    /**
     * 登录用户
     */
    public Map<String,Object> login(String username, String password){
        Map<String,Object> map = new HashMap<String,Object>();
        if(StringUtils.isBlank(username)){
            map.put("msgname","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msgpwd","密码不能为空");
            return map;
        }
        User user = userDao.selectByName(username);
        if(user == null){
            map.put("msgname","用户名不存在");
            return map;
        }
        if(!WxnewsUtil.MD5(password + user.getSalt()).equals(user.getPassword())){
            map.put("msgpwd","用户名或密码不正确");
            return map;
        }
        map.put("userId", user.getId());
        //ticket
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }


    /**
     * 设置Token
     * @param userId
     * @return
     */
    public String addLoginTicket(int userId){
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDao.addTicket(ticket);
        return ticket.getTicket();
    }


    /**
     * 注销用户
     */
    public void logout(String ticket){
        loginTicketDao.updateStatus(ticket,1);
    }


}
