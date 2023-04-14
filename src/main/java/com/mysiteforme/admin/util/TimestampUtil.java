package com.mysiteforme.admin.util;

import java.util.Date;

/**
 * @author yangqx
 * @description
 * @date 2023/4/12 11:32
 */
public class TimestampUtil {

    /**
     * 获取时间戳(毫秒级)
     * @param date
     * @return
     */
    public static Long getTimestamp(Date date) {
        return date.getTime();
    }

    /**
     * 获取时间戳(秒级)
     * @param date
     * @return
     */
    public static Long getTimestampSecond(Date date) {
        return date.getTime() / 1000;
    }

    /**
     * 获取当前时间戳(毫秒级)
     * @return
     */
    public static Long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * 时间戳转换为日期
     * @return
     */
    public static Date getDate(Long timestamp) {
        return new Date(timestamp);
    }

    /**
     * 时间戳（秒）转换为日期
     * @return
     */
    public static Date getDateSecond(Long timestamp) {
        return getDate(timestamp * 1000);
    }
}
