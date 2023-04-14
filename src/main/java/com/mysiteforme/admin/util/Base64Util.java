package com.mysiteforme.admin.util;

import java.util.Base64;

/**
 * @author yangqx
 * @description
 * @date 2023/4/12 10:25
 */
public class Base64Util {

    public Base64Util() {
    }

    public static byte[] decode(String str) {
        return Base64.getDecoder().decode(str);
    }

    public static String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] decodeMime(String str) {
        return Base64.getMimeDecoder().decode(str);
    }

    public static String encodeMime(byte[] bytes) {
        return Base64.getMimeEncoder().encodeToString(bytes);
    }

}
