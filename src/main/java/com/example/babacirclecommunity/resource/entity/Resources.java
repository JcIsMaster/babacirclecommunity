package com.example.babacirclecommunity.resource.entity;

import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class Resources {

    /**
     * ID
     */
    private int id;

    /**
     * 资源内容
     */
    private String content;

    /**
     * 一级标签ID
     */
    private Integer tagsOne;

    /**
     * 二级标签ID
     */
    private Integer tagsTwo;


    /**
     * 图片地址
     */
    private String[] img;

    /**
     *类型（0 图文  1视频）
     */
    private int type;

    /**
     * 用户id
     */
    private int uId;

    /**
     * 标题
     */
    private String title;

    /**
     * 视频地址
     */
    private String video;

    /**
     * 封面
     */
    private String cover;

    /**
     * 单元体类型
     */
    private int haplontType;

    /**
     * 点赞数量
     */
    private int favour;

    /**
     * 收藏数量
     */
    private int collect;

    /**
     * 浏览记录
     */
    private int browse;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 是否删除(1有效，0无效)
     */
    private int isDelete;
}
