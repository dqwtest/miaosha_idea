package com.example.miaosha.util;

import java.util.UUID;

public class UUIDUtil {
    public static String uuid() {
        // 去掉 - 符号
        return UUID.randomUUID().toString().replace("-", "");
    }
}
