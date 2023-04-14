package com.mysiteforme.admin.entity.vo.weidian;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yangqx
 * @description
 * @date 2023/4/14 16:00
 */
@Data
public class WeiDianProductVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;
    /**
     * 商品ID
     */
    private String item_id;

    /**
     * 商品名称
     */
    private String item_name;

    /**
     * 商品编码，用来关联爱奇艺商品
     */
    private String merchant_code;

    /**
     * 价格
     */
    private String price;

    /**
     * 总价
     */
    private String total_price;

    /**
     * 商品链接
     */
    private String url;
}
