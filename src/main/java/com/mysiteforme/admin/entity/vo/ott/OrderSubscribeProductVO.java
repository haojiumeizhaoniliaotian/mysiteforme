package com.mysiteforme.admin.entity.vo.ott;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yangqx
 * @description
 * @date 2023/4/12 10:42
 */
@Data
public class OrderSubscribeProductVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 包月产品或单点内容标识，双方约定配置，单点不能与包月订单产品ID相同（必填）
     */
    private String id;

    /**
     * 购买数量（必填，OTT厂商只能填写1）
     */
    private Integer quantity;

    /**
     * 爱奇艺侧的内容ID，产品类型为单点时有意义
     */
    private String cp_content_id;

    /**
     * 产品价格，值必须大于0，否则认为无效订单（单位分）
     */
    private Integer total_fee;
}
