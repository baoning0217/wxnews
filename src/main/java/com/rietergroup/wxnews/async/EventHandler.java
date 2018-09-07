package com.rietergroup.wxnews.async;

import java.util.List;

/**
 * created by baoning on 2018/9/5
 */
public interface EventHandler {

    void doHandle(EventModel model);

    List<EventType> getSupportEventTypes();


}
