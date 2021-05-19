package com.example.babacirclecommunity.common.exception;


import com.example.babacirclecommunity.common.constanct.CodeType;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author MQ
 * @Date 2019/8/31
 * @Version 1.0
 */
@Data
public class ApplicationException extends RuntimeException implements Serializable {

    private int code;
    private String msg;
    private int httpStatus;

    public ApplicationException() {
        super();
    }

    /**
     * 默认返回异常~
     * @param codeType
     */
    public ApplicationException(CodeType codeType) {
        this.code = codeType.getCode();
        this.msg = codeType.getMsg();
        this.httpStatus = codeType.getHttpStatus();
    }

    /**
     * 自定义返回异常
     * @param codeType
     * @param msg
     */
    public ApplicationException(CodeType codeType, String msg) {
        this.code = codeType.getCode();
        this.httpStatus = codeType.getHttpStatus();
        this.msg = msg;
    }
}
