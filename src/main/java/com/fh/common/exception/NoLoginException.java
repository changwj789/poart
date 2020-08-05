package com.fh.common.exception;


//自定义异常
public class NoLoginException extends Exception{

    public NoLoginException(String message){

        super(message);
    }
}
