package com.example.babacirclecommunity.user.vo;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/5/23 17:10
 */
@Data
public class UserVo {

    /**
     * 用户id
     */
    private int id;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String avatar;
}
