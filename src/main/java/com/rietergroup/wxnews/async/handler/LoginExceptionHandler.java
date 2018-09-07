package com.rietergroup.wxnews.async.handler;

import com.rietergroup.wxnews.async.EventHandler;
import com.rietergroup.wxnews.async.EventModel;
import com.rietergroup.wxnews.async.EventType;
import com.rietergroup.wxnews.model.Message;
import com.rietergroup.wxnews.service.MessageService;
import com.rietergroup.wxnews.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * created by baoning on 2018/9/6
 */
@Component
public class LoginExceptionHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    MailSender mailSender;

    public void doHandle(EventModel model) {
        //判断是否有异常登录
        Message message = new Message();
        message.setToId(model.getActorId());
        message.setContent("你上次的登录ip异常");
        message.setFromId(16);
        message.setCreateDate(new Date());
        messageService.addMessage(message);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", model.getExt("username"));

        mailSender.sendWithHTMLTemplate(model.getExt("email"), "登录异常", "mails/welcome.html", map);

    }


    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
