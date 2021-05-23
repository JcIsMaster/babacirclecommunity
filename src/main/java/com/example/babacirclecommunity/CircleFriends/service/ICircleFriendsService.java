package com.example.babacirclecommunity.CircleFriends.service;

import com.example.babacirclecommunity.CircleFriends.vo.CircleFriendsVo;

import java.util.List;

/**
 * @author MQ
 * @date 2021/4/6 13:39
 */
public interface ICircleFriendsService {

    /**
     * 得到朋友圈分享图
     * @return
     */
    List<String> selectCircleFriendsFigure(CircleFriendsVo circleFriendsVo);

    /**
     * 查询圈子海报图
     * @return
     */
    String[] queryCirclePoster();
}
