package com.rietergroup.wxnews;

import com.rietergroup.wxnews.service.LikeService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * created by baoning on 2018/9/7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WxnewsApplication.class)
public class LikeServiceTests {

    @Autowired
    LikeService likeService;


    @Test
    public void testLikeA(){

        likeService.like(123,1,1);
        Assert.assertEquals(1,likeService.getLikeStatus(123,1,1));

        System.out.println("testLikeA");

    }

    @Test
    public void testLikeB(){
        System.out.println("testLikeB");

    }

    @Before
    public void setUp(){
        System.out.println("setUp");

    }


    @After
    public void tearDown(){
        System.out.println("tearDown");

    }

    @BeforeClass
    public static void beforeClass(){
        System.out.println("beforeClass");

    }


    @AfterClass
    public static void afterClass(){
        System.out.println("afterClass");

    }






}
