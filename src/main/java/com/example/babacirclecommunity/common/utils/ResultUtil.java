package com.example.babacirclecommunity.common.utils;

import lombok.Data;

import java.io.Serializable;

/**
 *
 */
@Data
public class ResultUtil implements Serializable {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

    /**
     * 消息状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String msg;

    /**
     * 返回数据
     */
    private Object data;

    /**
     * 返回数据的数量
     */
    private int count;

    public ResultUtil(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


	public ResultUtil(Integer code, String msg, Object data, int count) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.count = count;
    }


    /**
     * 响应成功(带返回数据)
     * @param data 返回数据
     * @return Result
     */
    public static ResultUtil success(Object data,int count){
        return new ResultUtil(200,"成功",data,count);
    }
    
    /**
     * 响应成功(带返回数据)
     * @param data 返回数据
     * @return Result
     */
    public static ResultUtil success(Object data){
        return new ResultUtil(200,"成功",data);
    }
    
    /**
     * 响应成功
     * @return Result
     */
    public static ResultUtil success(Object data, String msg,Integer code){
        return new ResultUtil(code,msg,null);
    }
 
    /**
     * 响应错误(不带状态码,带消息)
     * @return Result
     */
    public static ResultUtil error(String msg){
        return new ResultUtil(240,msg,null);
    }
 
    /**
     * 响应错误(带错误码,消息提醒)
     * @return
     */
    public static ResultUtil errorMsg(Integer code, String msg){
        return new ResultUtil(code,msg,null);
    }
 

 
}