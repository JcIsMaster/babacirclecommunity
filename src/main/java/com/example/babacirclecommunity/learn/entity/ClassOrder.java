package com.example.babacirclecommunity.learn.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/5/15 10:15
 */
@Data
public class ClassOrder {

    private int id;
    /**
     * 公开课id
     */
    private int tId;
    /**
     * 购买人id
     */
    private int uId;
    /**
     * 购买价格
     */
    private int price;
    /**
     * 创建时间
     */
    private String createAt;
    /**
     * 1:有效；0:无效； 默认1
     */
    private int isDelete;
}
