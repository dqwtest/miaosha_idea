package com.example.miaosha.access;

import com.example.miaosha.domain.MiaoshaUser;

public class UserContext {
    public static ThreadLocal<MiaoshaUser> userHolder = new
            ThreadLocal<MiaoshaUser>();

    public static void setUser(MiaoshaUser user) {
        userHolder.set(user);
    }

    public static MiaoshaUser getUser() {
        return userHolder.get();
    }

    public static void removeUser() {
        userHolder.remove();
    }
}
