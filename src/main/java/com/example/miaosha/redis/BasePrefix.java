package com.example.miaosha.redis;

public class BasePrefix implements KeyPrefix {

    private int expireSeconds;
    private String prefix;

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    /**
     * 默为0代表永不过期
     * 返回key的过期时间
     * @return
     */
    public BasePrefix(String prefix) {
        this(0, prefix);
    }

    @Override
    public int expireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        // 避免重复
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }

    public void setExpireSeconds(int expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
