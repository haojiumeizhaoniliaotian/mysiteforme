package com.mysiteforme.admin.util;

import java.util.HashMap;

/**
 * ResponseBody构造器。一般用于ajax、rest等类型的Web服务
 */
@SuppressWarnings("serial")
public class WeiDianResponse extends HashMap<String, Object> {
    public static WeiDianResponse success(){
        return success("成功");
    }
    public static WeiDianResponse success(String message){
        WeiDianResponse restResponse = new WeiDianResponse();
        restResponse.setStatus("success");
        restResponse.setMessage(message);
        return restResponse;
    }

    public static WeiDianResponse failure(String message){
        WeiDianResponse restResponse = new WeiDianResponse();
        restResponse.setStatus("failure");
        restResponse.setMessage(message);
        return restResponse;
    }


    public WeiDianResponse setStatus(String success) {
        if (success != null) put("success", success);
        return this;
    }

    public WeiDianResponse setMessage(String message) {
        if (message != null) put("message", message);
        return this;
    }

    public WeiDianResponse setData(Object data) {
        if (data != null) put("data", data);
        return this;
    }

    public WeiDianResponse setPage(Integer page) {
        if (page != null) put("page", page);
        return this;
    }
    
    public WeiDianResponse setCurrentPage(Integer currentPage){
    	if (currentPage != null) put("currentPage", currentPage);
        return this;
    }

    public WeiDianResponse setLimit(Integer limit) {
        if (limit != null) put("limit", limit);
        return this;
    }

    public WeiDianResponse setTotal(Long total) {
        if (total != null) put("total", total);
        return this;
    }

    public WeiDianResponse setAny(String key, Object value) {
        if (key != null && value != null) put(key, value);
        return this;
    }
}
