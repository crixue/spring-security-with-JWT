package com.xrj.demo.common.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;


@Data
public class RestResp<T> {

    /**
     * 成功代码
     */
    public static final int CODE_SUCCESS = 0;
    /**
     * 失败代码(大于0，参考com.fanshan.lib.common.exception.code.ErrorCode)
     */
    public static final int CODE_FAILURE = 1;

    private Integer code;
    private T data;
    private String msg = "ok";
    private Long timestamp;

    public RestResp(){

    }

    public RestResp(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> RestResp ok(T data) {
        return new RestResp(CODE_SUCCESS, "ok", data);
    }

    public static <T> RestResp ok(String msg, T data) {
        return new RestResp(CODE_SUCCESS, msg, data);
    }

    public static <T> RestResp error(Integer code, String msg) {
        return new RestResp(code, msg, null);
    }

    public static <T> RestResp error(String msg) {
        return new RestResp(CODE_FAILURE, msg, null);
    }
    
    @JsonIgnore
    public boolean isSuccess() {
    		return this.code == CODE_SUCCESS;
    }
}
