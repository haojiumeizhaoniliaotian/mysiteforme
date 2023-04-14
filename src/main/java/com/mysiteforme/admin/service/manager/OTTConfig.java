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
public class OTTConfig {

    public static String PUBLIC_KEY;
    public static String PARTNER;

    /**
     * 爱奇艺提供的公钥
     */
    @Value("${aiqiy.publicKey}")
    private String publicKey;
    /**
     * 爱奇艺提供的合作方编码
     */
    @Value("${aiqiy.partner}")
    private String partner;

    @PostConstruct
    public void getEnvironment(){
        PUBLIC_KEY = publicKey;
        PARTNER = partner;
    }
}
