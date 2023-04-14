package com.mysiteforme.admin.entity.vo.ott;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yangqx
 * @description
 * @date 2023/4/12 10:37
 */
@Data
public class OrderSubscribeVO implements Serializable {
    private static final long serialVersionUID = 1L;

    public OrderSubscribeVO(){
        order_products = new ArrayList<>();
    }
    /**
     * OTT用户标志
     */
    private String user_id;
    /**
     * 开通权益的账号 （必填）
     */
    private String mobile;
    /**
     * 设备MAC
     */
    private String dev_mac;
    /**
     * 订单号 （必填）
     */
    private String order_id;
    /**
     * 订单金额，单位分（必填）
     */
    private Integer order_fee;
    /**
     * 订单支付时间，单位秒（必填）
     */
    private Long pay_time;
    /**
     * 优惠资格
     */
    private String pay_code;

    /**
     * 订单产品信息（必填） 虽然参数体为数组，但只能传一个产品信息，如果传多个，将只取第一个。
     */
    public List<OrderSubscribeProductVO> order_products;

}
