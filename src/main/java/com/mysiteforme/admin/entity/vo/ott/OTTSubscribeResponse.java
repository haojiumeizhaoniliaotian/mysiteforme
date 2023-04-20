package com.mysiteforme.admin.entity.vo.ott;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yangqx
 * @description
 * @date 2023/4/15 16:43
 */
@Data
public class OTTSubscribeResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    public OTTSubscribeResponse(){

    }

    /**
     * 状态编码  200 表示成功
     */
    private Integer err_code;

    /**
     * 成功填入OK，其他填写异常信息
     */
    private String err_msg;

    /**
     * 时间戳，单位秒
     */
    private Long time;

    private String gift_msg_code;

    private String gift_receive_deadline;

    private String gift_receive_url;

    /**
     * 是否自动赠送了vip， 0不赠送，1赠送黄金，2赠送星钻
     */
    private Integer present_gold_vip;



}
