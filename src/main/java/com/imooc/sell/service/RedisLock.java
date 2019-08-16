package com.imooc.sell.service;

import lombok.extern.slf4j.Slf4j;
import org.simpleframework.xml.core.Commit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

@Commit
@Slf4j
public class RedisLock {
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 加锁的方法
     * @param key
     * @param value 当前时间+超时时间
     * @return
     */
    public boolean lock(String key,String value) {
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
            return true;
        }
        //解除死锁
        String curValue = redisTemplate.opsForValue().get(key);
        //如果锁过期
        if (!StringUtils.isEmpty(curValue)
                &&Long.parseLong(curValue)<System.currentTimeMillis()) {
            //获取上一个锁的时间，并且解决同步问题
            String oldValue = redisTemplate.opsForValue().getAndSet(key,value);
            if (!StringUtils.isEmpty(oldValue)&&oldValue.equals(curValue)) {
                return true;
            }
        }
        return false;
    }

    public void unlock(String key,String value) {

        try {
            String curValue = redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(curValue)&&curValue.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            log.error("[redis分布式锁异常],e={}",e);
        }
    }
}
