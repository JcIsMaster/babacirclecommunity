package com.example.babacirclecommunity.circle.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/10/26 10:46
 */
@Data
public class GuessYouLike {

    /**
     * 用户id
     */
    private int id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户粉丝数量
     */
    private int followedNum;
}
