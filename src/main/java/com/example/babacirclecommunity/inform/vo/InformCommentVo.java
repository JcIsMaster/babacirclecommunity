package com.example.babacirclecommunity.inform.vo;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/6/18 16:17
 */
@Data
public class InformCommentVo {

    /**
     * 消息评论未读数量
     */
    private int commentNumber;

    /**
     * 消息点赞未读数量
     */
    private int thumbNumber;
}
