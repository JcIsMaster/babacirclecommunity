package com.example.babacirclecommunity.circle.entity;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/3/4 14:46
 * 二级评论实体类
 */
@Data
public class PostReply {

    private int id;

    /**
     *一级评论id
     */
    private int cId;

    /**
     *回复人ID
     */
    private int hId;

    /**
     *被回复人ID
     */
    private int bhId;

    /**
     *评论图片
     */
    private String hImage;

    /**
     *评论内容
     */
    private String hContent;

    /**
     *评论语音
     */
    private String hVoice;

    /**
     * 评论点赞状态（0没有，1点赞）
     */
    private int replyGiveStatus;

    /**
     *创建时间
     */
    private String createAt;

    /**
     *是否删除(1有效，0无效)默认1
     */
    private int isDelete;
}
