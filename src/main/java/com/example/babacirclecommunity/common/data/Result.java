package com.example.babacirclecommunity.common.data;

import com.example.babacirclecommunity.common.constanct.CodeType;
import lombok.Data;

import java.io.Serializable;

/**
 * 返回的结果封装
 * @Author MQ
 * @Date 2019/8/20
 * @Version 1.0
 */
@Data
public class Result<T> implements Serializable {

    /**
     * 状态码
     */
    private int code;

    /**
     * 结果描述
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    public Result(){
    }

    public Result(int code, String msg) {
        this(code,msg,null);
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(CodeType codeType){
        this(codeType.getCode(),codeType.getMsg(),null);
    }



    /**
     * 返回结果操作成功
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> SUCCESS(T data){
        Result<T> result = new Result<>();
        result.setCode(CodeType.SUCCESS.getCode());
        result.setData(data);
        result.setMsg(CodeType.SUCCESS.getMsg());
        return result;
    }


    /**
     * 错误返回
     * @param codeType
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(CodeType codeType) {
        Result<T> result = new Result<>();
        result.setCode(codeType.getCode());
        result.setMsg(codeType.getMsg());
        result.setData(null);
        return result;
    }

}
