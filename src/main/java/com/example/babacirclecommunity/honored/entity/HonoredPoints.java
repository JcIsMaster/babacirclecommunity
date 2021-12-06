package com.example.babacirclecommunity.honored.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/11/10 11:18
 */
@Data
public class HonoredPoints {

    public HonoredPoints(int userId, int points, int type, int source, String createAt) {
        this.userId = userId;
        this.points = points;
        this.type = type;
        this.source = source;
        this.createAt = createAt;
    }

    private int id;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 积分
     */
    private int points;

    /**
     * 类型（0.获得+  1.消费-）
     */
    private int type;

    /**
     * 积分来源（0.匹配  1.发帖  2.人才  3.货源  4.合作）
     */
    private int source;

    /**
     * 操作时间
     */
    private String createAt;
}
