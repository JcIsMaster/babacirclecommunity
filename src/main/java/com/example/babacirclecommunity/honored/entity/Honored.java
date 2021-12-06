package com.example.babacirclecommunity.honored.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/11/10 11:16
 */
@Data
public class Honored {

    private int id;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 等级（0.铜  1.银   2.金）
     */
    private int level;

    /**
     * 当前总积分
     */
    private int currentTotalPoints;
}
