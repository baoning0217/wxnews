package com.rietergroup.wxnews;

import com.rietergroup.wxnews.model.User;
import com.rietergroup.wxnews.util.JedisAdapter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * created by baoning on 2018/9/5
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WxnewsApplication.class)
public class RedisTest {

    @Autowired
    JedisAdapter jedisAdapter;

    @Test
    public void testObject(){

        User user = new User();
        user.setHeadUrl("http://114.255.95.20/oa/images/test.jpg");
        user.setName("user1");
        user.setPassword("123456");
        user.setSalt("salt");

        jedisAdapter.setObject("userlxx",user);

        User u = jedisAdapter.getObject("userlxx", User.class);
        System.out.println(ToStringBuilder.reflectionToString(u));



    }


}
