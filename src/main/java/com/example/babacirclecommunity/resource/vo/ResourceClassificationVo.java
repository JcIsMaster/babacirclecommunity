package com.example.babacirclecommunity.resource.vo;

import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class ResourceClassificationVo {

    private int id;


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
     * 浏览数量
     */
    private int browse;

    /**
     * 视频地址
     */
    private String video;

    /**
     * 封面
     */
    private String cover;


    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 标签Id
     */
    private int tagId;

    /**
     * 内容
     */
    private String content;




}
