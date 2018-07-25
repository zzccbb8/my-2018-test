package com.zcb.service;

/**
 * @author zhuchangbin
 * @date 2018/7/24
 */
public interface RedisService {

    boolean setNx(String key, String value);

    String getSet(String key, String value);

    String get(String key);

    boolean delete(String key);
}
