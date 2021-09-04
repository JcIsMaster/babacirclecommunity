package com.example.babacirclecommunity.learn.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/5/4 17:50
 */
@Data
public class PublicClassTagVo {

    private int id;
    /**
     * 标题
     */
    private String title;
    /**
     * 二级标签id
     */
    private int tagsTwo;
    /**
     * 二级标签名
     */
    private String tagName;
    /**
     * 封面图
     */
    private String coverImg;
    /**
     * 公开课价格 0为免费内容
     */
    private int price;
    /**
     * 课程详情介绍
     */
    private String description;
    /**
     * 公开课购买人数
     */
    private int buyerNum;
    /**
     * 课程是否删除
     */
    private int isDelete;
}
