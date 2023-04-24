package com.mysiteforme.admin.entity.vo.weidian;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author yangqx
 * @description
 * @date 2023/4/22 16:36
 */
@Data
public class OrderGetResultResponse implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 订单编号
     */
    private  String order_id;

    /**
     * 支付时间
     */
    private Date pay_time;

    /**
     * 订单价格
     */
    private String price;

    /**
     * 订单总价
     */
    private String total;

    /**
     * 卖家id
     */
    private String seller_id;

    /**
     * 卖家名称
     */
    private String seller_name;

    /**
     * 商品信息
     */
    private List<WeiDianProductVO> items;

    /**
     * 自定义字段， 目前只取name 为 ”充值手机号“
     */
    private List<WeiDianCustomFiledVO> customInfos;
}
