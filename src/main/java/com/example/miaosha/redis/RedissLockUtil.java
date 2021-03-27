package com.example.miaosha.redis;

import java.util.concurrent.TimeUnit;

public class RedissLockUtil {
    private static DistributedLocker redissLock;

    public RedissLockUtil(DistributedLocker redissLock) {
        redissLock = redissLock;
    }

    public static void lock(String lockKey) {
        redissLock.lock(lockKey);
    }

    public static void unlock(String lockKey) {
        redissLock.unlock(lockKey);
    }

    /**
     * 带超时的锁
     * @param lockKey
     * @param timeout 超时时间   单位：秒
     */
    public static void lock(String lockKey, int timeout) {
        redissLock.lock(lockKey, timeout);
    }

    /**
     * 带超时的锁
     * @param lockKey
     * @param unit 时间单位
     * @param timeout 超时时间
     */
    public static void lock(String lockKey, TimeUnit unit , int timeout) {
        redissLock.lock(lockKey, unit, timeout);
    }
}
