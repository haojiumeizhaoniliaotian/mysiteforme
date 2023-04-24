package com.mysiteforme.admin.entity.enums;

/**
 * @author yangqx
 * @description
 * @date 2023/4/15 15:43
 */
public enum OrderStatus {
    /**
    * 订单状态 1. 草稿、2. 微店已下单、3. 微店已支付、4. 已同步至爱奇艺、5. 爱奇艺已激活、-1.激活失败
	 * 11. 订单重复、 12. 产品信息不存在 13. 激活账号为空
     */
    ACTIVATE_FAILED(-1,"激活失败"),
    DRAFT(1,"草稿"),
    WEIDIAN_ORDERED(2,"微店已下单"),
    WEIDIAN_PAID(3,"微店已支付"),
    SYNCED(4,"已同步至爱奇艺"),
    ACTIVATED(5,"已激活"),
    ORDER_DUPLICATE(11,"订单重复"),
    PRODUCT_NOT_EXIST(12,"产品信息不存在"),
    ACCOUNT_EMPTY(13,"激活账号为空"),
    ORDER_NOT_EXISTS(14,"订单不存在"),
    NOT_TEST_CARD(98,"非测试卡"),
    OTHER(99,"其他");




    private final Integer code;
    private final String name;

    OrderStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getName(Integer code) {
        OrderStatus[] statuses = values();
        for (OrderStatus status : statuses) {
            if (status.getCode().equals(code)) {
                return status.getName();
            }
        }
        return null;
    }
}
