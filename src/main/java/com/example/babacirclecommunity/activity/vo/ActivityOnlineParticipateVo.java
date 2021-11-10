package com.example.babacirclecommunity.activity.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/10/13 16:42
 */
@Data
public class ActivityOnlineParticipateVo {

    /**
     * 活动参与表id
     */
    private int id;

    /**
     * 活动参与人id
     */
    private int userId;

    /**
     * 活动参与人昵称
     */
    private String userName;

    /**
     * 活动参与人头像
     */
    private String avatar;
}
