package com.example.babacirclecommunity.learn.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/4/28 13:59
 */
@Data
public class QuestionVo {

    private int id;
    /**
     * 标题
     */
    private String title;
    /**
     * 描述
     */
    private String description;
    /**
     * 0:图片  1:视频
     */
    private int contentType;
    /**
     * 封面图
     */
    private String coverImg;
    /**
     * 视频
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
     * 评论数量
     */
    private int comment;
    /**
     * 二级标签id
     */
    private int tagsTwo;
    /**
     * 提问tagsTwo对应的tagName
     */
    private String tagName;
}
