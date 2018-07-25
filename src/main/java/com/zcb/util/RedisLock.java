package com.zcb.util;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.Random;

/**
 * @author zhuchangbin
 * @date 2018/7/24
 */
public class RedisLock {

    private static Random random = new Random();

//    public static boolean lock(String lockKey, long expireTime, int timeout, boolean loop, RedisTemplate<String, String> redisTemplate) {
//
//        int count = 0;
//
//        String threadName = Thread.currentThread().getName();
//
//        while (timeout >= 0) {
//
//            //long expire = System.currentTimeMillis() + expireTime;
//
//            String expireStr = String.valueOf(expireTime);
//
//            if (redisTemplate.opsForValue().setIfAbsent(lockKey, expireStr)) {
//                return true;
//            }
//
//            // 不循环获取锁，直接退出
//            if (!loop) {
//                break;
//            }
//
//            String oldExpireTime = redisTemplate.opsForValue().get(lockKey);
//
//            if (oldExpireTime != null && Long.parseLong(oldExpireTime) < System.currentTimeMillis()) {
//                String oldValue = redisTemplate.opsForValue().getAndSet(lockKey, expireStr);
//                if (oldValue == null || oldValue.equals(oldExpireTime)) {
//                    return true;
//                }
//            }
//
//            // 没有获取锁, 循环等待
//            int t = random.nextInt(50);
//            timeout -= t;
//            try {
//                Thread.sleep(t);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            System.out.println(threadName + ", 没有获取锁，等待重试: " + ++count + " 次" + "， timeout=" + timeout);
//        }
//
//        return false;
//    }

    public static boolean lock_2(String lockKey, long expireTime, int timeout, boolean loop, RedisTemplate<String, String> redisTemplate) {

        int count = 0;

        String threadName = Thread.currentThread().getName();

        while (true) {

            //long expire = System.currentTimeMillis() + expireTime;

            String expireStr = String.valueOf(System.currentTimeMillis() + expireTime);

            if (redisTemplate.opsForValue().setIfAbsent(lockKey, expireStr)) {
                return true;
            }

            // 不循环获取锁，直接退出
            if (!loop) {
                break;
            }

            String oldExpireTime = redisTemplate.opsForValue().get(lockKey);

            if (oldExpireTime != null && Long.parseLong(oldExpireTime) < System.currentTimeMillis()) {
                String oldValue = redisTemplate.opsForValue().getAndSet(lockKey, expireStr);
                if (oldValue == null || oldValue.equals(oldExpireTime)) {
                    System.out.println("之前的线程已经过期，" + threadName + " 重新获取了锁。。。");
                    return true;
                }
            }

            // 没有获取锁, 循环等待
            int t = random.nextInt(50);
            timeout -= t;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //System.out.println(threadName + ", 没有获取锁，等待重试: " + ++count + " 次" + "， timeout=" + timeout);
            System.out.println(threadName + ", 没有获取锁，等待重试: " + ++count + " 次");
        }

        return false;
    }

    public static LockResp lock(String lockKey, long expireTime, int timeout, boolean loop, RedisTemplate<String, String> redisTemplate) {

        int count = 0;

        String threadName = Thread.currentThread().getName();

        while (true) {

            //long expire = System.currentTimeMillis() + expireTime;

            String expireStr = String.valueOf(System.currentTimeMillis() + expireTime);

            if (redisTemplate.opsForValue().setIfAbsent(lockKey, expireStr)) {
                System.out.println(threadName + ", 获取了锁, 锁状态：" + LockResp.success(Long.parseLong(expireStr)));
                return LockResp.success(Long.parseLong(expireStr));
            }

            // 不循环获取锁，直接退出
            if (!loop) {
                break;
            }

            String oldExpireTime = redisTemplate.opsForValue().get(lockKey);

            if (oldExpireTime != null && Long.parseLong(oldExpireTime) < System.currentTimeMillis()) {
                String oldValue = redisTemplate.opsForValue().getAndSet(lockKey, expireStr);

                System.out.println(threadName + " 发现之前的线程已经超时，进行getSet()操作, 原过期时间：" + oldExpireTime + ", getSet()返回的过期时间：" + oldValue + ", 新设置过期时间: " + expireStr);

                if (oldValue == null || oldValue.equals(oldExpireTime)) {
                    System.out.println("之前的线程的锁已经过期，" + threadName + " 重新获取了锁。。。");
                    return LockResp.success(Long.parseLong(expireStr));
                }
            }

            // 没有获取锁, 循环等待
            int t = random.nextInt(100);
            timeout -= t;
            try {
                Thread.sleep(t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //System.out.println(threadName + ", 没有获取锁，等待重试: " + ++count + " 次" + "， timeout=" + timeout);
            System.out.println(threadName + ", 没有获取锁，等待重试: " + ++count + " 次");
        }

        return LockResp.fail();
    }

    public static void unlock(String lockKey, long expireTime, RedisTemplate<String, String> redisTemplate) {
        String value = redisTemplate.opsForValue().get(lockKey);

        System.out.println(Thread.currentThread().getName() + ", key: " + lockKey + ", value: " + value + ", 传入值：" + expireTime);

        if (value != null && value.equals(String.valueOf(expireTime))) {
            System.out.println(Thread.currentThread().getName() + ", 是我的锁，删除了锁");
            redisTemplate.delete(lockKey);
        } else {
            System.out.println(Thread.currentThread().getName() + ", 不是我的锁，锁已经没了, 或者已经超时了, 直接返回");
        }

//        if (value != null && value.equals(String.valueOf(expireTime))) {
//            System.out.println(Thread.currentThread().getName() + ":释放了锁");
//            redisTemplate.delete(lockKey);
//        } else {
//            System.out.println(Thread.currentThread().getName() + ":锁已经没了，或者已经超时了");
//        }
    }
}
