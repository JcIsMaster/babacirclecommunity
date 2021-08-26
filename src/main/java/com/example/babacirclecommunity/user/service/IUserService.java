package com.example.babacirclecommunity.user.service;

import com.example.babacirclecommunity.user.entity.User;
import com.example.babacirclecommunity.user.vo.UserLoginVo;
import com.example.babacirclecommunity.user.vo.UserVo;

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
    User wxLogin(String code, String userName, String avatar, String address, int sex);

    /**
     * 查询用户历史浏览、粉丝、关注数量
     * @param userId
     * @return
     */
     UserLoginVo loginVo(int userId);

    /**
     * 根据id查询用户部分用户信息 （消息版块用的接口）
     * @param id 他的用id
     * @param userId 当前登录人id
     * @return
     */
    UserVo queryUserInformationBasedUserId(int id, int userId);

    /**
     * 根据用户id插叙所有用户信息字段
     * @param userId
     * @return
     */
    User queryUserById(int userId);

    /**
     * 查询用户信息和金币信息
     * @param userId 用户id
     * @return
     */
    User selectUserById(int userId);
}
