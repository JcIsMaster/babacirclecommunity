package com.example.babacirclecommunity.CircleFriends.service;

import com.example.babacirclecommunity.CircleFriends.vo.CircleFriendsVo;

import java.util.List;

/**
 * @author MQ
 * @date 2021/4/6 13:39
 */
public interface ICircleFriendsService {

    /**
     *  得到朋友圈分享图
     * @param pageUrl 二维码指向的地址
     * @param id 帖子id
     * @return
     */
    List<String> selectCircleFriendsFigure(String pageUrl,String id);

    /**
     * 查询圈子海报图
     * @return
     */
    String[] queryCirclePoster();
}
