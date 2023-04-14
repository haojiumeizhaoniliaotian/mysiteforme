package com.mysiteforme.admin.service.manager;

import com.alibaba.fastjson.JSON;
import com.mysiteforme.admin.controller.integrate.WeiDianController;
import com.mysiteforme.admin.entity.Order;
import com.mysiteforme.admin.entity.vo.ott.OrderSubscribeProductVO;
import com.mysiteforme.admin.entity.vo.ott.OrderSubscribeVO;
import com.mysiteforme.admin.util.Base64Util;
import com.mysiteforme.admin.util.HttpClientUtil;
import com.mysiteforme.admin.util.TimestampUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author yangqx
 * @description
 * @date 2023/4/12 10:11
 */
public class OTTManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(OTTManager.class);

    public static final String SIGN_ALGORITHMS="SHA1WithRSA";

    private static final String HOST = "http://openapi.vip.iqiyi.com";
    private static final String ORDER_SUBSCRIBE_URL = "/ott/subscribe.action";

    /**
     * 订单同步接口
     * @param order
     */
    public static String subscribe(Order order){
        OrderSubscribeVO orderSubscribeVO = new OrderSubscribeVO();

        orderSubscribeVO.setMobile(order.getAccount());
        orderSubscribeVO.setOrder_id(wrapOrderNo(order.getOrderNo()));
        BigDecimal price = order.getPrice();
        if(Objects.isNull(price)){
            price = BigDecimal.ONE; // 为0会导致订单失效
        }
        orderSubscribeVO.setOrder_fee((price.multiply(BigDecimal.valueOf(100))).intValue());
        orderSubscribeVO.setPay_time(TimestampUtil.getTimestampSecond(order.getPlaceOrderTime()));

        OrderSubscribeProductVO productVO = new OrderSubscribeProductVO();
        // YLSXYX07test
        productVO.setId("YLSXYX07test");
        productVO.setQuantity(1);
        productVO.setTotal_fee((price.multiply(BigDecimal.valueOf(100))).intValue());
        orderSubscribeVO.order_products.add(productVO);
        String privateKey = null;
        String result = null;
        try {
            privateKey = getPrivateKey();

            String data = JSON.toJSONString(orderSubscribeVO);
            data = Base64Util.encode(data.getBytes());
            String sign = sign(data, privateKey, "utf-8");

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("partner", OTTConfig.PARTNER);
            paramsMap.put("signature", sign);
            paramsMap.put("data", data);
            Map<String, String> headParams = new HashMap<>();
            headParams.put("Content-Type", "application/json");
            HttpResponse response = null;

            response = HttpClientUtil.doPostForm(HOST, ORDER_SUBSCRIBE_URL, null, null, paramsMap);
            result = EntityUtils.toString(response.getEntity(), "UTF-8");
            Map<String, String> commonRsp = JSON.parseObject(result, HashMap.class);
            if(commonRsp.containsKey("data")){
                result = new String(Base64Util.decode(commonRsp.get("data")),"UTF-8");
            }

        } catch (IOException e) {
            LOGGER.error("订单同步异常{}", e);
        }

        LOGGER.info("订单同步返回结果{}", result);

        return result;
    }


    public static String wrapOrderNo(String orderNo){
        // 构建固定长度的订单号
        String prefix = "sairuq";


        return prefix + "_test_" + orderNo;
    }

    public static String sign(String content, String privateKey, String encode){
        String charset = "utf-8";
        if(!StringUtils.isEmpty(encode)){
            charset = encode;
        }
        try{
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64Util.decodeMime(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes(charset));
            byte[] signed = signature.sign();
            return Base64Util.encodeMime(signed);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static String getPrivateKey() throws IOException {
        Resource resource = new ClassPathResource("rsa/pkcs8_rsa_private_key.pem");
        System.out.println(resource.getFile().getPath());
        BufferedReader br = new BufferedReader(new FileReader(resource.getFile().getPath()));
        /**
         * 这个巨重要，会筛选掉密钥内容中的首行标识字段
         */
        String s = br.readLine();
        String str = "";
        s = br.readLine();
        while (s.charAt(0) != '-') {
            str += s + "\r";
            s = br.readLine();
        }
        return str.toString();
    }

    private static String getPublicKey() throws IOException {
        Resource resource = new ClassPathResource("rsa/pkcs8_rsa_public_key.pem");
        System.out.println(resource.getFile().getPath());
        BufferedReader br = new BufferedReader(new FileReader(resource.getFile().getPath()));
        /**
         * 这个巨重要，会筛选掉密钥内容中的首行标识字段
         */
        String s = br.readLine();
        String str = "";
        s = br.readLine();
        while (s.charAt(0) != '-') {
            str += s + "\r";
            s = br.readLine();
        }
        return str.toString();
    }
}
