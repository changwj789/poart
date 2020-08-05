package com.fh.common;

public enum RedisType {
    TYPE_STRING("字符串","string"),
    TYPE_HASH("hash","hash"),
    TYPE_LIST("LIST集合","list"),
    TYPE_SET("set集合","set"),
    TYPE_ZSET("zset","zset"),
    ;


    private String cn;
    private String type;

    RedisType(String cn, String type) {
        this.cn = cn;
        this.type = type;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
