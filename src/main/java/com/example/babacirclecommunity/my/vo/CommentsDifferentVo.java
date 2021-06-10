package com.example.babacirclecommunity.my.vo;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/6/10 10:56
 */
@Data
public class CommentsDifferentVo {

    /**
     * 帖子id
     */
    private int id;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 帖子封面
     */
    private String cover;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 评论时间
     */
    private String createAt;

    /**
     * 帖子是否被删除
     */
    private int isDelete;

    /**
     * 类型名字
     */
    private String typeName;

}
