package com.rietergroup.wxnews.async.handler;

import com.rietergroup.wxnews.async.EventHandler;
import com.rietergroup.wxnews.async.EventModel;
import com.rietergroup.wxnews.async.EventType;
import com.rietergroup.wxnews.model.Message;
import com.rietergroup.wxnews.model.User;
import com.rietergroup.wxnews.service.MessageService;
import com.rietergroup.wxnews.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * created by baoning on 2018/9/5
 */
@Component
public class LikeHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(16);
        //message.setToId(model.getEntityOwnerId());
        model.setActorId(model.getActorId());
        User user = userService.getUser(model.getActorId());
        message.setContent("用户" + user.getName() + "赞了你的资讯，http://localhost:8080/news/" + model.getEntityId());
        message.setCreateDate(new Date());
        messageService.addMessage(message);
    }


    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }


}
