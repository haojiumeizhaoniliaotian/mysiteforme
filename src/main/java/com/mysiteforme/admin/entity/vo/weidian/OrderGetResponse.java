package com.mysiteforme.admin.entity.vo.weidian;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yangqx
 * @description
 * @date 2023/4/22 16:35
 */
@Data
public class OrderGetResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    public OrderGetResultResponse result;

    public WeiDianStatusResponse status;
}
