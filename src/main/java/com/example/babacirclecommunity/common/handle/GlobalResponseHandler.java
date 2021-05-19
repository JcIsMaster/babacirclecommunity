package com.example.babacirclecommunity.common.handle;


import com.example.babacirclecommunity.common.anntation.IgnoreResponseAdvice;
import com.example.babacirclecommunity.common.data.Result;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局统一返回
 * @Author MQ
 * @Date 2019/8/31
 * @Version 1.0
 */
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice {



    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        if (methodParameter.getMethod().isAnnotationPresent(IgnoreResponseAdvice.class) ||
                aClass.isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }

        /**
         * 配置swagger
         */
        if (methodParameter.getMember().getName().equals("error") || methodParameter.getMember().getName().equals("uiConfiguration")
              || methodParameter.getMember().getName().equals("securityConfiguration") || methodParameter.getMember().getName().equals("swaggerResources")
              || methodParameter.getMember().getName().equals("getDocumentation")) {
            return false;
        }
        return true;
    }

    @Override
    public Result beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (o instanceof ApplicationException) {
            ApplicationException exception = (ApplicationException) o;
            Result result = new Result();
            result.setCode(exception.getCode());
            result.setMsg(exception.getMsg());

            return result;
        }

        return Result.SUCCESS(o);
    }





}
