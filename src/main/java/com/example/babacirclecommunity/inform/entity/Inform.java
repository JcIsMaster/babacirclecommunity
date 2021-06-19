package com.example.babacirclecommunity.inform.entity;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/3/22 10:43
 * 通知实体类
 */
@Data
public class Inform {

    /**
     * 主键
     */
    private int id;

    /**
     * 内容
     */
    private String content;

    /**
     * 通知人id
     */
    private int notifierId;

    /**
     * 被通知人id
     */
    private int notifiedPartyId;

    /**
     * 帖子id
     */
    private int tId;

    /**
     * 通知类型（0评论我，1获赞）
     */
    private int informType;

    /**
     * 帖子类型 (0圈子，1提问，2干货)
     */
    private int oneType;

    /**
     * 是否已读 （0未读 1已读） 默认0
     */
    private int readUnread;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 是否有效（1有效，0无效）默认1
     */
    private int isDelete;

}
