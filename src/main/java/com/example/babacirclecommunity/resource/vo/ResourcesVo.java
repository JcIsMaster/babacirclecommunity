package com.example.babacirclecommunity.resource.vo;

import lombok.Data;

/**
 * @author MQ
 * @create 2021/2/22
 **/
@Data
public class ResourcesVo {


    private int id;

    /**
     * 圈子内容
     */
    private String content;

    /**
     * 一级标签id
     */
    private int tagsOne;

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
     * 用户名称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 标题
     */
    private String title;

    /**
     * 视频地址
     */
    private String video;


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
     * 标签名称
     */
    private String tagName;

    /**
     * 标签Id
     */
    private int tagId;

    /**
     * 点过赞人的头像
     */
    private String[] giveAvatar;


    /**
     * 是否收藏 （1收藏，0没有）
     */
    private int whetherCollection;

    /**
     * 观看过人的头像
     */
    private String[] browseAvatar;

    /**
     * 封面
     */
    private String cover;
}
