package com.example.babacirclecommunity.learn.entity;

import lombok.Data;

/**
 * @author JC
 */
@Data
public class DryGoods {

    private int id;
    /**
     * 发帖人id
     */
    private int uId;
    /**
     * 标题
     */
    private String title;
    /**
     * 一级标签id
     */
    private int tagsOne;
    /**
     * 二级标签id
     */
    private int tagsTwo;
    /**
     * 单元体类型id
     */
    private int haplontId;
    /**
     * 点赞数量
     */
    private int favour;
    /**
     * 收藏数量
     */
    private int collect;
    /**
     * 描述
     */
    private String description;
    /**
     * 封面图
     */
    private String coverImg;
    /**
     * 内容
     */
    private String content;
    /**
     * 删除状态1:有效；0:无效； 默认1
     */
    private int isDelete;
    /**
     * 发布时间
     */
    private String createAt;
    
}
