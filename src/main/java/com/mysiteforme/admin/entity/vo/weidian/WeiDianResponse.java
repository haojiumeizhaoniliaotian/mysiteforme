package com.mysiteforme.admin.entity.vo.weidian;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yangqx
 * @description
 * @date 2023/4/22 16:45
 */
@Data
public class WeiDianResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private T result;

    private WeiDianStatusResponse status;
}
