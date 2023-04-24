package com.mysiteforme.admin.service.manager;

import com.alibaba.fastjson.JSON;
import com.mysiteforme.admin.controller.integrate.WeiDianController;
import com.mysiteforme.admin.entity.Order;
import com.mysiteforme.admin.entity.vo.ott.OTTSubscribeResponse;
import com.mysiteforme.admin.entity.vo.ott.OrderSubscribeProductVO;
import com.mysiteforme.admin.entity.vo.ott.OrderSubscribeVO;
import com.mysiteforme.admin.util.Base64Util;
import com.mysiteforme.admin.util.HttpClientUtil;
import com.mysiteforme.admin.util.TimestampUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.math.BigDecimal;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.MessageFormat;
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

    private static final Map<String, String> productMap = new HashMap<>();

    private static final Map<String, String> productToOrderMap = new HashMap<>();


    static{
        productMap.put("IQY-TEST-01SXZY", OTTConfig.TEST_CARD);
        productMap.put("IQY-42Y-01SXZY", OTTConfig.FORMAL_YEAR_CARD);
        productMap.put("IQY-42M-01SXZY", OTTConfig.FORMAL_MONTH_CARD);
        productMap.put("IQY-42Y-000000", OTTConfig.FORMAL_YEAR_CARD);
        productMap.put("IQY-42M-000000", OTTConfig.FORMAL_MONTH_CARD);
        productMap.put("IQY-42H-000000", OTTConfig.TEST_CARD);
        productMap.put("IQY-42Q-000000", OTTConfig.TEST_CARD);
        productMap.put("IQY-42D-000000", OTTConfig.TEST_CARD);

        productToOrderMap.put(OTTConfig.TEST_CARD, "TT");
        productToOrderMap.put(OTTConfig.FORMAL_YEAR_CARD, "42Y");
        productToOrderMap.put(OTTConfig.FORMAL_MONTH_CARD, "42M");
    }

    /**
     * 订单同步接口
     * @param order
     */
    public static OTTSubscribeResponse subscribe(Order order, String productNo){
        OrderSubscribeVO orderSubscribeVO = new OrderSubscribeVO();

        orderSubscribeVO.setMobile(order.getAccount());
        orderSubscribeVO.setOrder_id(wrapOrderNo(order.getOrderNo(), productNo));
        BigDecimal price = order.getPrice();
        if(Objects.isNull(price)){
            price = BigDecimal.ONE; // 为0会导致订单失效
        }
        orderSubscribeVO.setOrder_fee((price.multiply(BigDecimal.valueOf(100))).intValue());
        orderSubscribeVO.setPay_time(TimestampUtil.getTimestampSecond(order.getPlaceOrderTime()));

        OrderSubscribeProductVO productVO = new OrderSubscribeProductVO();
        // YLSXYX07test
        productVO.setId(productNo);
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
            order.setOttOrderNo(orderSubscribeVO.getOrder_id());

        } catch (IOException e) {
            LOGGER.error("订单同步异常{}", e);
            return null;
        }
        LOGGER.info("订单同步返回结果{}", result);

        OTTSubscribeResponse response = JSON.parseObject(result, OTTSubscribeResponse.class);

        return response;
    }

    /**
     * 构建订单OTT订单号，用分隔符区分，如 SW_42Y_821175645287756
     * 第一位序位，用两位表示字符表示，字符一固定为S，表示为赛锐琪；字符二表示不同客户端，A表示微店...
     * 第二位序位表示产品类型，42Y、42M等，测试表示 TT
     * 第三位表示客户端推送过来的订单编号
     * @param orderNo
     * @return
     */
    public static String wrapOrderNo(String orderNo){
        // 构建固定长度的订单号
        String prefix = "S";

        return prefix + "_test_" + orderNo;
    }

    /**
     * 构建订单OTT订单号，用分隔符区分，如 SW_42Y_821175645287756
     * 第一位序位，用两位表示字符表示，字符一固定为S，表示为赛锐琪；字符二表示不同客户端，A表示微店...
     * 第二位序位表示产品类型，42Y、42M等，测试表示 TT, 未匹配默认为NT
     * 第三位表示客户端推送过来的订单编号
     * @param orderNo
     * @return
     */
    public static String wrapOrderNo(String orderNo, String productNo){
        // 构建固定长度的订单号
        String prefix = "SA";

        String productType = "NT";
        if(productToOrderMap.containsKey(productNo)){
            productType = productToOrderMap.get(productNo);
        }

        return MessageFormat.format("{0}_{1}_{2}", prefix, productType, orderNo);
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

        ClassPathResource classPathResource = new ClassPathResource("rsa/pkcs8_rsa_private_key.pem");
        InputStream inputStream = classPathResource.getInputStream();

      //  File privateKey = ResourceUtils.getFile("classpath:rsa/pkcs8_rsa_private_key.pem");

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
    //    BufferedReader br = new BufferedReader(new FileReader(privateKey));
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

    /**
     * 映射产品编码
     */
    public static String mapProductNo(String productNo){
        if(productMap.containsKey(productNo)){
            return productMap.get(productNo);
        }
        return null;
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
