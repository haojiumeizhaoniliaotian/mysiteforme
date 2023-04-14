package com.mysiteforme.admin.entity.vo.weidian;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yangqx
 * @description
 * @date 2023/4/13 17:24
 */
@Data
public class AccessTokenResponse implements Serializable {

        private static final long serialVersionUID = 1L;

        public AccessTokenResponse(){

        }

        private AccessTokenResultResponse result;

        private AccessTokenStatusResponse status;
}
