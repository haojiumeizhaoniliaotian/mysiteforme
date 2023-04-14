package com.mysiteforme.admin.controller.integrate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mysiteforme.admin.annotation.SysLog;
import com.mysiteforme.admin.base.BaseController;
import com.mysiteforme.admin.entity.Order;
import com.mysiteforme.admin.entity.vo.request.OrderCreateVO;
import com.mysiteforme.admin.entity.vo.weidian.WeiDianOrderVO;
import com.mysiteforme.admin.redis.RedisWeiDianDAO;
import com.mysiteforme.admin.service.manager.OTTManager;
import com.mysiteforme.admin.service.manager.WeiDianConfig;
import com.mysiteforme.admin.util.RestResponse;
import com.mysiteforme.admin.util.UUIDUtil;
import com.mysiteforme.admin.util.WeiDianResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by yangqx on 2023年4月10日
 *
 */
@Controller
@RequestMapping("integrate/weidian")
public class WeiDianController extends BaseController{
    private static final Logger LOGGER = LoggerFactory.getLogger(WeiDianController.class);

    @Autowired
    private RedisWeiDianDAO redisWeiDianDAO;

    @PostMapping("receive/form")
    @ResponseBody
    @SysLog("微店消息订阅地址")
    public WeiDianResponse receive(HttpServletRequest request){
        String content = request.getParameter("content");
        if(StringUtils.isEmpty(content)){
            LOGGER.error("WeiDian.receive.form.error: 接收到了一个空字符串。");
            return WeiDianResponse.success();
        }
        // message type
        Map<String, Object> contentMap = JSON.parseObject(content, HashMap.class);
        if(!contentMap.containsKey("type")){
            LOGGER.error("WeiDian.receive.form.error: 未识别到Type类型。content：" + content);
            return WeiDianResponse.success();
        }

        if(!Objects.equals(contentMap.get("type"), WeiDianConfig.MESSAGE_TYPE)){
            LOGGER.error("WeiDian.receive.form.error: 接收到了非订单完成的字符串。content：" + content);
            return WeiDianResponse.success();
        }
        LOGGER.info("微店消息订阅form:"+ content);
        // 序列化 message 参数，获取订单号、支付时间、完成时间、价格（price）、扩展字段
        WeiDianOrderVO weiDianOrderVO = JSON.parseObject(JSON.toJSONString(contentMap.get("message")), WeiDianOrderVO.class);

        String contextId = UUIDUtil.randomUUID();

        Order order = wrapOrder(weiDianOrderVO);
        order.setRequestContent(content);
        order.setContextId(contextId);

        orderService.saveOrder(order);

        if(order.getId() == null || order.getId() == 0){
            LOGGER.error("WeiDian.receive.form.save.error: 同步订单信息出错。content：" + content);
            return WeiDianResponse.success();
        }
        /**

        if(StringUtils.isNotEmpty(order.getAccount())){
            // 拥有激活手机号，则直接推爱奇艺
            // 推送爱奇艺
            String result = OTTManager.subscribe(order);
        }
         */


        return WeiDianResponse.success();
    }

    /**
     * 构建订单数据
     * @param weiDianOrderVO
     * @return
     */
    private Order wrapOrder(WeiDianOrderVO weiDianOrderVO){
        // 定义备注
        // 获取手机号
        // 处理金额
        //
        BigDecimal price = null;
        String account = null;
        StringBuilder sb = new StringBuilder();
        Order order = new Order();

        try {
            price = new BigDecimal(weiDianOrderVO.getPrice());
        } catch (Exception e) {
            price = new BigDecimal(0.01);
            sb.append("金额转换异常；");
        }
        if(CollectionUtils.isEmpty(weiDianOrderVO.getCustomInfos())){
            sb.append("扩展字段为空；");
        }else {
           // weiDianOrderVO.getCustomInfos() 转换成 Map<String, String>, name 是key， value 是 value
            Map<String, String> customInfos = weiDianOrderVO.getCustomInfos().stream().collect(HashMap::new, (m, v) -> m.put(v.getName(), v.getValue()), HashMap::putAll);
            if(customInfos.containsKey("充值手机号")){
                account = customInfos.get("充值手机号");
            }else {
                sb.append("未获取到激活手机号；");
            }
        }
        order.setOrderNo(weiDianOrderVO.getOrder_id());
        order.setPlaceOrderTime(weiDianOrderVO.getPay_time());
        order.setPrice(price);
        order.setAccount(account);
        order.setRemarks(sb.toString());
        order.setDelFlag(Boolean.FALSE);
        // 订单状态 1. 草稿、2. 微店已下单、3. 微店已支付、4. 已同步至爱奇艺、5. 爱奇艺已激活、-1.激活失败
        order.setStatus(3);
        return order;
    }


    @PostMapping("receive/json")
    @ResponseBody
    @SysLog("微店消息订阅地址")
    public WeiDianResponse receiveJson(@RequestBody Object content){
        LOGGER.info("微店消息订阅json:"+ JSONObject.toJSONString(content));
        return WeiDianResponse.success();
    }


    @PostMapping("receive/entity")
    @ResponseBody
    @SysLog("微店消息订阅地址")
    public WeiDianResponse receiveEntity(@RequestBody OrderCreateVO orderVO){
        LOGGER.info("微店消息订阅entity:"+ JSONObject.toJSONString(orderVO));
        Order order = new Order();
        BeanUtils.copyProperties(orderVO, order);

        // 设置是否删除，下单时间、订单状态、上下文id
        order.setDelFlag(Boolean.FALSE);
        order.setStatus(1);
        order.setContextId(UUIDUtil.randomUUID());
        orderService.saveOrder(order);
        if(order.getId() == null || order.getId() == 0){
            return WeiDianResponse.failure("同步订单信息出错。");
        }
        // 推送爱奇艺
        String result = OTTManager.subscribe(order);

        return WeiDianResponse.success();
    }

    /**
     * 获取微店的token，redis中存在则使用，否则使用密钥获取，并存入到redis中
     * @return
     */
    @GetMapping("flush/token")
    @ResponseBody
    public RestResponse getAccessToken(){
        RestResponse response = RestResponse.failure("获取token失败");

        String token = redisWeiDianDAO.getAccessToken();
        if(StringUtils.isNotEmpty(token)){
            response = RestResponse.success();
            response.setData(token);
        }
        return response;
    }


}
