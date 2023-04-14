package com.mysiteforme.admin.service.manager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author yangqx
 * @description
 * @date 2023/4/12 14:10
 */
@Configuration
public class WeiDianConfig {

    public static String APP_KEY;
    public static String SECRET;
    public static String MESSAGE_TYPE;
    /**
     * app_key
     */
    @Value("${weidian.appKey}")
    private String appKey;
    /**
     * 密钥
     */
    @Value("${weidian.secret}")
    private String secret;

    /**
     * 消息类型
     */
    @Value("${weidian.messageType}")
    private String messageType;

    @PostConstruct
    public void getEnvironment(){
        APP_KEY = appKey;
        SECRET = secret;
        MESSAGE_TYPE = messageType;
    }
}
