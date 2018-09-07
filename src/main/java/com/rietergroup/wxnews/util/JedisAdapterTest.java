package com.rietergroup.wxnews.util;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

public class JedisAdapterTest {
/**
    public static void print(int index, Object obj){
        System.out.println(String.format("%d,%s", index, obj.toString()));
    }

    public static void main(String[] args){
        Jedis jedis = new Jedis("192.168.65.206",6379);
        jedis.flushAll();

        jedis.set("hello","world");
        print(1,jedis.get("hello"));
        jedis.rename("hello", "newhello");
        print(1, jedis.get("newhello"));

        jedis.setex("hello2",15,"world2");

        //
        jedis.set("pv","100");
        jedis.incr("pv");
        print(2,jedis.get("pv"));
        jedis.incrBy("pv",5);
        print(2,jedis.get("pv"));

        //列表操作
        String listName = "listA";
        for(int i=0; i<10; i++){
            jedis.lpush(listName, "a" + String.valueOf(i));
        }
        print(3,jedis.lrange(listName,0,12));
        print(4,jedis.llen(listName));
        print(5,jedis.lpop(listName));
        print(6,jedis.llen(listName));
        print(7,jedis.lindex(listName,3));
        print(8,jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER,"a4","xx"));
        print(9,jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE,"a4","bb"));
        print(10,jedis.lrange(listName,0,12));

        //
        String userKey = "userxx";
        jedis.hset(userKey, "name", "jim");
        jedis.hset(userKey, "age", "12");
        jedis.hset(userKey,"phone","13012340000");
        print(11,jedis.hget(userKey,"name"));
        print(12,jedis.hgetAll(userKey));
        jedis.hdel(userKey,"phone");
        print(13,jedis.hgetAll(userKey));
        print(14,jedis.hkeys(userKey));
        print(15,jedis.hvals(userKey));
        print(16,jedis.hexists(userKey,"email"));
        print(17,jedis.hexists(userKey,"age"));
        jedis.hsetnx(userKey,"school","tiancheng");
        jedis.hsetnx(userKey,"name","yxy");
        print(18,jedis.hgetAll(userKey));

        //set

        //Pool,默认8条线程,需要关闭线程迟
        JedisPool pool = new JedisPool("192.168.65.206",6379);
        for(int i=0; i < 100; i++){
            Jedis j = pool.getResource();
            j.get("a");
            System.out.println("POOL:" + i);
            j.close();
        }

    }

**/
}
