package com.rietergroup.wxnews.async;

import com.alibaba.fastjson.JSONObject;
import com.rietergroup.wxnews.util.JedisAdapter;
import com.rietergroup.wxnews.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * created by baoning on 2018/9/5
 */
@Service
public class EventProducer {

    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel model){
        try{
            String json = JSONObject.toJSONString(model);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        }catch (Exception e){
            return false;
        }
    }





}
