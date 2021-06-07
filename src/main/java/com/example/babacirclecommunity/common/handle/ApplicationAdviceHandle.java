package com.example.babacirclecommunity.common.handle;


import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.data.Result;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author MQ
 * @Date 2019/8/31
 * @Version 1.0
 */
@RestControllerAdvice
public class ApplicationAdviceHandle extends ResponseEntityExceptionHandler {


    /**
     * 自定义返回异常
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler(value = ApplicationException.class)
    public ApplicationException applicationExceptionHandle (final ApplicationException e, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, X-CSRF-TOKEN, X-Requested-With, Content-Type, Accept");
        return e;
    }

    /**
     * 其他异常
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ApplicationException exceptionHandle (final ApplicationException e, HttpServletResponse response) {
        response.setStatus(CodeType.UNKNOWN_ERROR.getHttpStatus());
        e.printStackTrace();
        return new ApplicationException(CodeType.UNKNOWN_ERROR);
    }

    @ExceptionHandler(value = MultipartException.class)
    Result arithmeticExceptionException(MultipartException e, HttpServletRequest request) {
        return new Result(-1,"上传单个文件大小只能上传30M","");
    }


}
