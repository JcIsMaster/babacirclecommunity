package com.example.babacirclecommunity.resource.entity;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/3/2 17:21
 * 收藏实体类
 */
@Data
public class Collection {

    private int id;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 帖子id
     */
    private int tId;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 备注
     */
    private String remarks;

    /**
     * （1收藏，0取消收藏）
     */
    private int isDelete;
}
