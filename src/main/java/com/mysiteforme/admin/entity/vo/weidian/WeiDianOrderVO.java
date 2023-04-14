package com.mysiteforme.admin.entity.vo.weidian;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yangqx
 * @description
 * @date 2023/4/14 16:00
 */
@Data
public class WeiDianOrderVO implements Serializable {
    private static final long serialVersionUID = 1L;

    public WeiDianOrderVO(){
        items = new ArrayList<>();
        customInfos = new ArrayList<>();
    }

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
     * 商品信息
     */
    private List<WeiDianProductVO> items;

    /**
     * 自定义字段， 目前只取name 为 ”充值手机号“
     */
    private List<WeiDianCustomFiledVO> customInfos;

}
