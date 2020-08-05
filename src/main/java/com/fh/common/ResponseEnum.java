package com.fh.common;

public enum ResponseEnum {

    REG_MEMBER_IS_MAIL(1001,"信息不完全！！！"),
    REG_MEMBERName_IS_MAIL(1002,"用户名存在！！！"),
    ;

    private int code;
    private String msg;

    private ResponseEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
