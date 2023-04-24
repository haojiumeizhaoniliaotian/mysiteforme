package com.mysiteforme.admin.entity.vo.weidian;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yangqx
 * @description
 * @date 2023/4/22 16:36
 */
@Data
public class OrderGetStatusResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 返回结果状态， 0 成功
     */
    private Integer status_code;

    private String status_reason;
}
