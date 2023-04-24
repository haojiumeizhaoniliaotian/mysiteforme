package com.mysiteforme.admin.entity.vo.weidian;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yangqx
 * @description
 * @date 2023/4/13 17:24
 */
@Data
public class WeiDianStatusResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    public WeiDianStatusResponse(){
    }

    private Integer status_code;

    private String status_reason;
}
