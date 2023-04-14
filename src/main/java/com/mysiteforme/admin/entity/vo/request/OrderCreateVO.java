package com.mysiteforme.admin.entity.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yangqx
 * @description
 * @date 2023/4/11 22:54
 */
@Data
public class OrderCreateVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 下单时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date placeOrderTime;

    /**
     * 激活账号
     */
    private String account;

    /**
     * 订单金额
     */
    private BigDecimal price;

    /**
     * 请求上下文
     */
    private String requestContent;
}
