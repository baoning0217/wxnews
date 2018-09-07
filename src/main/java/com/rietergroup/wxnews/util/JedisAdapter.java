package com.rietergroup.wxnews.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Service
public class JedisAdapter implements InitializingBean{

    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private JedisPool pool = null;

    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("192.168.65.206",6379);
    }


    private Jedis getJedis(){
        return pool.getResource();
    }


    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.get(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long sadd(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sadd(key,value);
        }catch(Exception e){
            logger.error("发生异常" + e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    //移除集合中一个或多个成员
    public long srem(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.srem(key,value);
        }catch(Exception e){
            logger.error("发生异常" + e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    //判断成员元素是否是集合的成员
    public boolean sismember(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sismember(key,value);
        }catch(Exception e){
            logger.error("发生异常" + e.getMessage());
            return false;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    //获取集合的成员数
    public long scard(String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.scard(key);
        }catch(Exception e){
            logger.error("发生异常" + e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    //将一个或多个值插入到列表头部
    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    //移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    public void setObject(String key , Object obj){
        set(key, JSON.toJSONString(obj));
    }


    public <T> T getObject(String key, Class<T> clazz){
        String value = get(key);
        if(value != null){
            return JSON.parseObject(value, clazz);
        }
        return null;
    }



}
