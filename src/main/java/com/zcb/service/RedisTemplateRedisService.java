package com.zcb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author zhuchangbin
 * @date 2018/7/24
 */
@Service
public class RedisTemplateRedisService implements RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean setNx(String key, String value) {



        return false;
    }

    @Override
    public String getSet(String key, String value) {
        return null;
    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public boolean delete(String key) {
        return false;
    }
}
