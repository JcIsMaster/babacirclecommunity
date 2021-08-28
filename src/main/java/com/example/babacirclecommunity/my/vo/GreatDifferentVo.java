package com.example.babacirclecommunity.my.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/8/28 13:55
 */
@Data
public class GreatDifferentVo {

    /**
     * 帖子id
     */
    private int id;

    /**
     * 发帖人id
     */
    private int userId;

    /**
     * 发帖人名字
     */
    private String userName;

    /**
     * 帖子封面
     */
    private String cover;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 点赞时间
     */
    private String createAt;

    /**
     * 帖子是否被删除
     */
    private int isDelete;

    /**
     * 类型名字
     */
    private int typeName;

    /**
     * 0 图文 1视频
     */
    private int type;
}
