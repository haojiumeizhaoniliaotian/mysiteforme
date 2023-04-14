package com.mysiteforme.admin.util;

import java.util.UUID;

/**
 * @author yangqx
 * @description
 * @date 2023/4/8 13:35
 */
public class UUIDUtil {
    public UUIDUtil() {
    }

    public static String randomUUID(boolean rmLine, boolean upperCase) {
        String uuid = generateUUID();
        uuid = rmLine ? uuid.replaceAll("-", "") : uuid;
        uuid = upperCase ? uuid.toUpperCase() : uuid;
        return uuid;
    }

    public static String randomUUID() {
        return randomUUID(true, false);
    }

    private static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
