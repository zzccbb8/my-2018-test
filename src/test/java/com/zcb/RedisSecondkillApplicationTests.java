package com.zcb;

import com.zcb.util.LockResp;
import com.zcb.util.RedisLock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisSecondkillApplicationTests {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void contextLoads() {
        String lockKey = "lock_user";
        int expireTime = 8 * 1000;


        for (int i = 0; i < 3; i++) {
//            new Thread(() -> {
//                if (RedisLock.lock(lockKey, expireTime, 0, false, redisTemplate)) {
//                    System.out.println("线程:" + Thread.currentThread().getName() + ", 获取了锁========");
//                    RedisLock.unlock(lockKey, expireTime, redisTemplate);
//                    System.out.println("线程:" + Thread.currentThread().getName() + ", 删除了锁========");
//                } else {
//                    System.out.println("线程:" + Thread.currentThread().getName() + ", 没有获取锁");
//                }
//            }, "线程" + i).start();

            int finalI = i;
            new Thread(() -> {
                //long expire = System.currentTimeMillis() + expireTime;
                long expire = expireTime;
                //System.out.println("当前线程：" + Thread.currentThread().getName() + "_" + finalI);
                LockResp lockResp = RedisLock.lock(lockKey, expire, 100, true, redisTemplate);
                if (lockResp.isFlag()) {
                    System.out.println(Thread.currentThread().getName() + ", 获取了锁========");

                    // 模拟处理业务
                    try {
                        System.out.println(Thread.currentThread().getName() + ", 我正在处理业务******************");
                        Thread.sleep(2 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + ", 业务处理完毕了******************");

                    RedisLock.unlock(lockKey, lockResp.getExpireTime(), redisTemplate);

                    System.out.println(Thread.currentThread().getName() + ", 释放了锁========");
                } else {
                    System.out.println(Thread.currentThread().getName() + ", 没有获取锁");
                }
            }, "线程_" + finalI).start();

        }

        try {
            Thread.sleep(30 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("主程序退出了...");
    }

}
