package com.example.babacirclecommunity.learn.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/4/29 14:24
 */
@Data
public class QuestionTagVo {

    private int id;
    /**
     * 发问人id
     */
    private int uId;
    /**
     * 发问人名称
     */
    private String uName;
    /**
     * 发问人头像
     */
    private String avatar;
    /**
     * 标题
     */
    private String title;
    /**
     * 一级标签id
     */
    private int tagsOne;
    /**
     * 二级标签id
     */
    private int tagsTwo;
    /**
     * 单元体类型id
     */
    private int haplontId;
    /**
     * 点赞数量
     */
    private int favour;
    /**
     * 收藏数量
     */
    private int collect;
    /**
     * 评论数量
     */
    private int comment;
    /**
     * 描述
     */
    private String description;
    /**
     * 是否匿名 0:否  1:是
     */
    private int anonymous;
    /**
     * 0:图片  1:视频
     */
    private int contentType;
    /**
     * 封面图
     */
    private String coverImg;
    /**
     * 视频
     */
    private String video;
    /**
     * 悬赏奖励  0.未悬赏
     */
    private int award;
    /**
     * 删除状态1:有效；0:无效； 默认1
     */
    private int isDelete;
    /**
     * 发布时间
     */
    private String createAt;
    /**
     * 提问tagsTwo对应的tagName
     */
    private String tagName;
    /**
     * 收到金币数量
     */
    private int goldNum;
    /**
     * 看贴人对该帖子的点赞状态 0:未点赞； 1:已点赞
     */
    private int whetherGive;
    /**
     * 看贴人对该帖子的收藏状态 0:未收藏； 1:已收藏
     */
    private int whetherCollect;
}
