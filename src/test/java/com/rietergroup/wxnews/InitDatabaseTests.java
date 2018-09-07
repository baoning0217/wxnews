package com.rietergroup.wxnews;

import com.rietergroup.wxnews.dao.CommentDao;
import com.rietergroup.wxnews.dao.NewsDao;
import com.rietergroup.wxnews.dao.UserDao;
import com.rietergroup.wxnews.model.News;
import com.rietergroup.wxnews.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.util.Date;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WxnewsApplication.class)
public class InitDatabaseTests {

	@Autowired
	UserDao userDao;

	@Autowired
	NewsDao newsDao;

	@Autowired
	CommentDao commentDao;

	@Test
	public void contextLoads() {
		for(int i =0;i<4;i++){

			User user = new User();
			user.setName(String.format("User%d",i));
			user.setPassword("");
			user.setSalt("");
			user.setHeadUrl("");
			userDao.addUSer(user);

			News news = new News();
			news.setTitle(String.format("Title{%d}",i));
			news.setLink(String.format("https://www.baidu.com"));
			news.setImage("");
			news.setLikeCount(i+1);
			news.setCommentCount(i);
			Date date = new Date();
			date.setTime(date.getTime() + i);
			news.setCreateDate(date);
			news.setUserId(i);
			newsDao.addNews(news);

		}

	}

}
