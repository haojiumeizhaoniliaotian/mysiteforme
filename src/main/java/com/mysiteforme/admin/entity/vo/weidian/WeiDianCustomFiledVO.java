package com.mysiteforme.admin.entity.vo.weidian;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yangqx
 * @description
 * @date 2023/4/14 16:06
 */
@Data
public class WeiDianCustomFiledVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自定义字段名称
     */
    private String name;

    /**
     * 自定义字段值
     */
    private String value;

    /**
     * 自定义字段类型
     */
    private String format;

}
