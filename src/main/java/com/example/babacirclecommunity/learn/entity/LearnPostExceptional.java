package com.example.babacirclecommunity.learn.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/5/12 17:25
 */
@Data
public class LearnPostExceptional {

    private int id;
    /**
     * 干货帖子id
     */
    private int tId;
    /**
     * 打赏金币数量
     */
    private int goldNum;
    /**
     * 打赏人id
     */
    private int rewarderId;
    /**
     * 帖子类型0:提问;1:干货;2:公开课
     */
    private int type;
    /**
     * 创建时间
     */
    private String createAt;
}
