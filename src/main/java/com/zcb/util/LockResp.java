package com.zcb.util;

/**
 * @author zhuchangbin
 * @date 2018/7/25
 */
public class LockResp {

    private boolean flag;
    private long expireTime;

    private LockResp() {

    }

    private LockResp(boolean flag, long expireTime) {
        this.flag = flag;
        this.expireTime = expireTime;
    }

    public static LockResp success(long expireTime) {
        return new LockResp(true, expireTime);
    }

    public static LockResp fail() {
        return new LockResp(false, 0);
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
}
