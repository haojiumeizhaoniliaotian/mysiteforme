package com.mysiteforme.admin.service.manager;

import com.alibaba.fastjson.JSON;
import com.mysiteforme.admin.entity.vo.weidian.AccessTokenResponse;
import com.mysiteforme.admin.util.HttpClientUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author yangqx
 * @description
 * @date 2023/4/13 17:12
 */
public class WeiDianManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeiDianManager.class);


    private static final String HOST = "https://oauth.open.weidian.com";
    private static final String ORDER_SUBSCRIBE_URL = "/token";



    public static String getAccessToken(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        Map<String, String> querys = new HashMap<>();
        querys.put("grant_type", "client_credential");
        querys.put("appkey", WeiDianConfig.APP_KEY);
        querys.put("secret", WeiDianConfig.SECRET);
        String token = null;
        try {
            String result = null;
            HttpResponse response = HttpClientUtil.doGet(HOST, ORDER_SUBSCRIBE_URL, headers, querys);
            result = EntityUtils.toString(response.getEntity(), "UTF-8");
            LOGGER.info("获取微店token返回结果：{}", result);
            AccessTokenResponse tokenResponse = JSON.parseObject(result, AccessTokenResponse.class);

            if(Objects.nonNull(tokenResponse) && Objects.nonNull(tokenResponse.getStatus())
                    && Objects.equals(tokenResponse.getStatus().getStatus_code(), 0)){
                token = tokenResponse.getResult().getAccess_token();
            }

        } catch (Exception e) {
            LOGGER.error("获取微店token异常：{}", e);
        }
        return token;
    }



}
