package com.fh.common;

import com.fh.common.exception.CountException;
import com.fh.common.exception.NoLoginException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ControllerException {

    /**
     * 处理所有异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ServerResponse handleException(Exception e){

        return ServerResponse.error(500,e.getMessage());
    }

    @ExceptionHandler(NoLoginException.class)
    @ResponseBody
    /*@CrossOrigin*/
    public ServerResponse handleNoLoginException(NoLoginException e){
        return ServerResponse.error(1000,e.getMessage());
    }

    @ExceptionHandler(CountException.class)
    @ResponseBody
    public ServerResponse handleCountException(CountException e){
        return ServerResponse.error(2000,e.getMessage());
    }
}
