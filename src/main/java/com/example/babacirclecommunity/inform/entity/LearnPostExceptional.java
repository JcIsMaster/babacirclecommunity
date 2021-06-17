package com.example.babacirclecommunity.inform.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/4/16 15:45
 */
@Data
public class LearnPostExceptional {

    /**
     * id
     */
    private int id;
    /**
     * 帖子id
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
     * 创建时间
     */
    private String create_at;
    /**
     * 帖子类型0:提问;1:干货;2:公开课
     */
    private int type;

}
