package com.example.babacirclecommunity.inform.vo;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/3/22 10:55
 */
@Data
public class InformUserVo {

    /**
     * 通知人id
     */
    private int userId;

    /**
     * 通知人名称
     */
    private String userName;

    /**
     * 通知人头像
     */
    private String avatar;

    /**
     * 通知id
     */
    private int id;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论时间
     */
    private String createAt;

    /**
     * 帖子id
     */
    private int tId;

    /**
     * 帖子封面
     */
    private String cover;

    /**
     * 帖子内容
     */
    private String title;

    /**
     * 帖子类型(0圈子，1提问，2干货)
     */
    private int oneType;

    /**
     * 帖子是否删除  0 删除 1没有删除
     */
    private int isDelete;


}
