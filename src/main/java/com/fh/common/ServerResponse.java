package com.fh.common;

import java.io.Serializable;

public class ServerResponse implements Serializable {

    private int code;

    private String msg;

    private Object data;

    private ServerResponse() {

    }

    private ServerResponse(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    private ServerResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    private ServerResponse(String msg) {
        this.code = 200;
        this.msg = msg;
    }
    private ServerResponse(String msg,Object data) {
        this.code = 200;
        this.msg = msg;
        this.data=data;
    }
    public static ServerResponse success(String msg,Object data) {

        return new ServerResponse(msg,data);
    }
    public static ServerResponse success(String msg) {
        return new ServerResponse(msg);
    }
    public static ServerResponse success(int code, String msg){return new ServerResponse(code, msg);}
    public static ServerResponse success() {
        return new ServerResponse(200, "ok", null);
    }

    public static ServerResponse success(Object data) {
        return new ServerResponse(200, "ok", data);
    }

    public static ServerResponse error() {
        return new ServerResponse(-1, "操作失败", null);
    }

    public static ServerResponse error(int code, String msg) {
        return new ServerResponse(code, msg, null);
    }

    public static ServerResponse error(ResponseEnum responseEnum) {
        return new ServerResponse(responseEnum.getCode(), responseEnum.getMsg(), null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
