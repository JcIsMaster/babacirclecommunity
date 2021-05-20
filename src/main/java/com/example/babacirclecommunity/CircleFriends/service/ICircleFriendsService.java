package com.example.babacirclecommunity.CircleFriends.service;

import java.util.List;

/**
 * @author MQ
 * @date 2021/4/6 13:39
 */
public interface ICircleFriendsService {

    /**
     * 得到朋友圈分享图
     * @param headUrl 头像地址
     * @param postImg 帖子第一张图片
     * @param postContent 帖子内容
     * @param userName 用户名
     * @param pageUrl  二维码指向地址
     * @param title  标题
     * @return
     */
    List<String> selectCircleFriendsFigure(String headUrl, String postImg, String postContent, String userName,String pageUrl,String title);

    /**
     * 查询圈子海报图
     * @return
     */
    String[] queryCirclePoster();
}
