package com.example.babacirclecommunity.user.service;

import com.example.babacirclecommunity.user.entity.User;

/**
 * @author MQ
 * @date 2021/5/21 13:24
 */
public interface IUserService {

    /**
     * 小程序登陆
     * @param code
     * @param userName 用户明个
     * @param avatar 头像
     * @param address 地址
     * @param sex 性别
     * @return
     */
    User wxLogin(String code, String userName, String avatar, String address, String sex);
}