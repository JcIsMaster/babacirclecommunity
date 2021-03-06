package com.example.babacirclecommunity.circle.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author Administrator
 */
@Data
public class CircleClassificationVo {

    private int id;

    /**
     *类型（0 图文  1视频）
     */
    private int type;

    /**
     * 发帖人id
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
     * 用户性别
     */
    private int userSex;

    /**
     * 图片组
     */
    private String[] img;

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
     * 内容
     */
    private String content;

    /**
     * 标签Id
     */
    private int tagId;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 是否点赞 （1点赞 0没有点赞）
     */
    private int whetherGive=0;

    /**
     * 是否关注 （1关注 0没有关注）
     */
    private int whetherAttention=0;

    /**
     * 点赞数量
     */
    private int giveNumber;

    /**
     * 点过赞人的头像
     */
    private String[] giveAvatar;

    /**
     * 帖子评论数量
     */
    private int numberPosts;

    /**
     * 转发数量
     */
    private int forwardingNumber;

    /**
     * 发帖地址
     */
    private String address;

    /**
     * 是否精选
     */
    private int isFeatured;

    /**
     * 推荐的帖子
     * 当这个字段为空时，不返回前端
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CircleClassificationVo> recommends;
}
