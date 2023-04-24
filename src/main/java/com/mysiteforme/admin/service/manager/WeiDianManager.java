package com.mysiteforme.admin.service.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mysiteforme.admin.entity.vo.weidian.*;
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


    private static final String API_HOST = "https://api.vdian.com";
    private static final String API_URL = "/api";
    private static final String ORDER_GET = "vdian.order.get";


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

    /**
     * 校验微店订单是否存在
     * @param orderNo
     * @param token
     * @return
     */
    public static Boolean checkWeiDianOrderIsExists(String orderNo, String token){
        return Objects.nonNull(getOrderDetail(orderNo, token));
    }

    /**
     * 获取订单详情
     * @param orderNo
     * @param token
     */
    public static OrderGetResultResponse getOrderDetail(String orderNo, String token){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("order_id", orderNo);

        Map<String, String> publicMap = wrapPublicParam(token, ORDER_GET);

        Map<String, String> querys = new HashMap<>();
        querys.put("param", JSONObject.toJSONString(paramMap));
        querys.put("public", JSONObject.toJSONString(publicMap));
        try {
            String result = null;
            HttpResponse response = HttpClientUtil.doGet(API_HOST, API_URL, headers, querys);
            result = EntityUtils.toString(response.getEntity(), "UTF-8");
            LOGGER.info("获取微店订单详情返回结果：{}", result);

            OrderGetResponse orderGetResponse = JSON.parseObject(result, OrderGetResponse.class);

            if(Objects.nonNull(orderGetResponse) && Objects.nonNull(orderGetResponse.getStatus())
                    && Objects.equals(orderGetResponse.getStatus().getStatus_code(), 0)){
                return orderGetResponse.getResult();
            }

        } catch (Exception e) {
            LOGGER.error("获取微店订单详情异常：{}", e);
        }
        return null;
    }


    private static Map<String, String> wrapPublicParam(String token, String method){
        Map<String, String> publicMap = new HashMap<>();
        publicMap.put("method", method);
        publicMap.put("access_token", token);
        publicMap.put("version", "1.0");
        publicMap.put("format", "json");
        return publicMap;
    }


}
